url = "http://localhost:8080/text";

const reloadBtn = document.querySelector("#reload");
reloadBtn.addEventListener("click", setClipboard);

async function getData(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

async function setClipboard() {
    getData(url)
        .then(data => {
            printDataToClipboard(data.text);
        })
        .catch(error => {
            console.error(error);
        });
}

async function printDataToClipboard(data) {
    // document.getElementById("title").innerHTML=data;
    await navigator.clipboard.writeText(data);
}

const pasteBtn = document.querySelector("#paste");
pasteBtn.addEventListener("click", executePasteScript);

async function executePasteScript() {
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        chrome.scripting.executeScript({
            target: { tabId: tabs[0].id },
            files: ["paste.js"]
        });
    });
}