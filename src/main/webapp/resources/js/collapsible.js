window.addEventListener("load", e => {
	apply();
});

function apply() {
	const ele = [...document.querySelectorAll(".collapsible")];
	ele.forEach( e => {
		let visible = false;
		
		//[...e.children].forEach( c => c.classList.add("hide"));
		e.classList.add("hide");
		const title = document.createElement("div");
		title.classList.add("fas", "fa-plus-circle", "collapsible-title");
		title.textContent = e.dataset.title;
		title.addEventListener("click", ev => {
			if(visible) {
				title.classList.toggle("fa-plus-circle");
				title.classList.toggle("fa-minus-circle");
				//[...e.children].forEach( c => c.classList.add("hide"));
				e.classList.add("hide");
			} else {
				title.classList.toggle("fa-plus-circle");
				title.classList.toggle("fa-minus-circle");
				//[...e.children].forEach( c => c.classList.remove("hide"));
				e.classList.remove("hide");
			}
			visible = !visible;
		});
		e.parentElement.insertBefore(title,e);
	});
}

apply();