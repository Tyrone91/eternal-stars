package eternal.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UITable<T> {
    
    private Optional<ColumnHeader<T>> pivotHeader = Optional.empty();
    private boolean sortReverse = false;;
    private List<Row<T>> rows = new ArrayList<>();
    private List<ColumnHeader<T>> headers = new ArrayList<>();
    private Map<String, ColumnHeader<T>> columnNamesToHeader = new HashMap<>();
    private int entriesPerPage = 5;
    private int currentPage = 0;
    private Supplier<Collection<T>> dataGetter;
    private BiFunction<T, Row<T>, Row<T>> dataMapper;
    
    private UITable() {
        
    }
    
    public synchronized List<ColumnHeader<T>> getHeaders() {
        return this.headers;
    }
    
    public synchronized List<Row<T>> getRows() {
        final List<Row<T>> list = new ArrayList<>(this.rows);
        if(!pivotHeader.isPresent()) {
            return getSubList(list);
        }
        
        final ColumnHeader<T> header = pivotHeader.get();
        if(!header.getComparator().isPresent()) {
            return getSubList(list);
        }
        
        final RowComparator<T> c = header.getComparator().get(); 
        
        list.sort( (r1, r2) -> {
            final Row<T> first = this.sortReverse ? r2 : r1;
            final Row<T> second = this.sortReverse ? r1 : r2;
            return c.compareBy(header.getName(), first.getValue(), second.getValue());
        });
        
        return getSubList(list);
    }
    
    public boolean isActionRendered() {
        return this.getRows().stream().map(Row::getActions).anyMatch(not(List::isEmpty));
    }
    
    private <TT> Predicate<TT> not(Predicate<TT> p) {
        return p.negate();
    }
    
    public synchronized UITable<T> updateData() {
        pullData(dataGetter, dataMapper);
        return this;
    }
    
    private synchronized List<Row<T>> getSubList(List<Row<T>> data) {
        final int from = entriesPerPage * currentPage;
        final int to = from + entriesPerPage;
        return data.subList(from, to > rows.size() ? rows.size() : to);
    }
    
    public synchronized List<Integer> getPossiblePages() {
        if(this.rows.isEmpty()) {
            return Collections.emptyList();
        }
        int entries = (this.rows.size() / entriesPerPage);
        if((this.rows.size() % entriesPerPage) == 0) {
            --entries;
        }
        List<Integer> res = new ArrayList<>(entries);
        for(int i = 0; i <= entries; ++i) {
            res.add(i);
        }
        return res;
    }
    
    public synchronized void setEntriesPerPage(int maxEntries) {
        this.entriesPerPage = maxEntries;
    }
    
    public synchronized boolean isSelectedPage(int index) {
        return this.currentPage == index;
    }
    
    public synchronized void setSelectedPage(int page) {
        if(page < 0 ) {
            return;
        }
        
        int last = (this.rows.size() / entriesPerPage);
        if((this.rows.size() % entriesPerPage) != 0) {
            ++last;
        }
        if(last <= page) {
            return;
        }
        this.currentPage = page;
    }
    
    public synchronized void goPageRight() {
        setSelectedPage(currentPage+1);
    }
    
    public synchronized void goPageLeft() {
        setSelectedPage(currentPage-1);
    }
    
    private void pullData(Supplier<Collection<T>> rows, BiFunction<T, Row<T>, Row<T>> mapper) {
        this.rows = rows.get().stream().map( obj -> {
            final Row<T> r = new Row<>(this);
            return mapper.apply(obj, r);
        }).collect(Collectors.toList());
        this.rows.forEach(Row::afterNewData);
    }
    
    private UITable<T> finish(Supplier<Collection<T>> rows, BiFunction<T, Row<T>, Row<T>> mapper) {
        this.dataGetter = rows;
        this.dataMapper = mapper;
        pullData(dataGetter, dataMapper);
        return this;
    }
    
    private void addColumn(String name, Optional<RowComparator<T>> comparator) {
        final ColumnHeader<T> header = new ColumnHeader<>(this, name, comparator);
        if(columnNamesToHeader.put(name, header) != null) {
            throw new RuntimeException("Duplicate column");
        }
        
        this.headers.add(header);
    }
    
    private void sortBy(ColumnHeader<T> header) {
        if(header == null) {
            pivotHeader = Optional.empty();
            return;
        }
        this.sortReverse = pivotHeader.map( h -> h == header && !this.sortReverse).orElse(false);
        this.pivotHeader = Optional.ofNullable(header);
    }
    
    private ColumnHeader<T> getHeader(String name) {
        final ColumnHeader<T> header = this.columnNamesToHeader.get(name);
        if(header == null) {
            throw new RuntimeException("Column not defined");
        }
        return header;
    }
    
    public static class RowAction<T> {
        private String title;
        private String icon;
        private T target;
        private Consumer<T> onClick;
        
        public void onClick() {
            if(onClick == null || target == null) {
                return;
            }
            onClick.accept(target);
        }
        
        public String getIcon() {
            return this.icon;
        }
        
        public String getTitle() {
            return this.title;
        }
    }
    
    public static class Row<T> {
        
        private UITable<T> parent;
        private Map<ColumnHeader<T>, Column<T>> columns = new HashMap<>();
        private List<RowAction<T>> actions = new ArrayList<>();
        private T obj;
        
        public Row(UITable<T> parent) {
            this.parent = parent;
        }
        
        
        
        public Row<T> set(String column, T obj, Function<T,String> toString) {
            final ColumnHeader<T> header = parent.getHeader(column);
            final Column<T> col = new Column<>(header, toString.apply(obj));
            this.obj = obj;
            columns.put(header, col);
            return this;
        }
        
        public Row<T> action(String icon, String title, Consumer<T> onclick) {
            final RowAction<T> action = new RowAction<>();
            action.title = title;
            action.icon = icon;
            action.onClick = onclick;
            actions.add(action);
            return this;
        }
        
        private void afterNewData() {
            this.actions.forEach(a -> a.target = obj);
        }
       
        public List<Column<T>> getColumns() {
            return this.parent.headers.stream().map(this::getColumn).collect(Collectors.toList());
        }
        
        public List<RowAction<T>> getActions() {
            return this.actions;
        }
        
        public Column<T> getColumn(ColumnHeader<T> header) {
            return this.columns.get(header);
        }
        
        public T getValue() {
            return this.obj;
        }
    }
    
    public static class ColumnHeader<T> {
        
        private Optional<RowComparator<T>> comparator;
        private String name;
        private UITable<T> parent;
        
        public ColumnHeader(UITable<T> parent, String name, Optional<RowComparator<T>> comp) {
            this.parent = parent;
            this.name = name;
            this.comparator = comp;
        }
        
        public String getName() {
            return this.name;
        }
        
        public void sort() {
            this.parent.sortBy(this);
        }
        
        public boolean isSortable() {
            return comparator.isPresent();
        }
        
        public Optional<RowComparator<T>> getComparator() {
            return this.comparator;
        }
        
        public boolean isReverseSorted() {
            return this.parent.sortReverse;
        }
        
        public boolean isSelected() {
            return this.parent.pivotHeader.map(this::isMe).orElse(false);
        }
        
        private boolean isMe(ColumnHeader<T> header) {
            return header == this;
        }
    }
    
    public static class Column<T> {
        
        private ColumnHeader<T> header;
        private String value;
        
        public Column(ColumnHeader<T> header, String value) {
            this.header = header;
            this.value = value;
        }
        
        public String getName() {
            return this.header.getName();
        }
        
        public String getValue() {
            return this.value;
        }
        
        public boolean isSortable() {
            return this.header.isSortable();
        }
        
        public void sort() {
            this.header.sort();
        }
        
    }
    
    public static interface RowComparator<T> {
        public int compareBy(String fieldName, T first, T second);
    }
    
    public static class Builder<T> {
        
        private UITable<T> parent;
        
        private Builder() {
            parent = new UITable<>();
        }
        
        public Builder<T> add(String column) {
            return add(column, null);
        }
        
        public Builder<T> add(String column, RowComparator<T> comparator) {
            parent.addColumn(column, Optional.ofNullable(comparator));
            return this;
        }
        
        public UITable<T> build(Supplier<Collection<T>> rows, BiFunction<T, Row<T>, Row<T>> mapper) {
            final UITable<T> table = this.parent.finish(rows, mapper);
            this.parent = null;
            return table;
        }
    }
    
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
    
    private static class Test {
        public String id;
        public String desc;
        
        public Test(String id,String desc ) {
            this.id = id;
            this.desc = desc;
        }
    }
    
    public static List<Test> getList() {
        return Arrays.asList(new Test("1", "hi"), new Test("2", "bye"));
    }
}
