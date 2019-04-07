let RESOURCE_PULL = true;

function pullResources() {
	const button = document.getElementById("placeholder-resource-pull-button");

	const request = () => {
		button.click();
		if(RESOURCE_PULL) {
			setTimeout(request,500);
		}
	};

	request();
}

function stopPullingResources() {
	RESOURCE_PULL = false;
}

window.addEventListener("load", event => {
	console.log("found");
	pullResources();
	
});

