let RESOURCE_PULL_INVTERVALL_ID = -1;

function pullResources() {
	const button = document.getElementById("placeholder-resource-pull-button");
	button.style = "display: none";
	RESOURCE_PULL_INVTERVALL = setInterval( () => {
		button.click();
	}, 500);
}

function stopPullingResources() {
	console.log("Hello World");
	clearInterval(RESOURCE_PULL_INVTERVALL_ID);
}

window.addEventListener("load", event => {
	pullResources();
	console.log("JS loaded");
});