import { fetchData } from "./api.js";
import { person } from "./person.js";
import { highlightSelected } from "./highlight.js";
import { removeHighlight } from "./highlight.js";


document.addEventListener("DOMContentLoaded", function () {
    reloadData();
    executeScript(highlightSelected);
});

document.addEventListener("visibilitychange", function () { executeScript(removeHighlight) });

// const reloadBtn = document.querySelector("#reload");
// reloadBtn.addEventListener("click", reloadData);

async function reloadData() {
    hideContent();
    showLoading();
    fetchData()
        .then(data => {
            person.setData(data);
            actualizeData();
            hideLoading();
            showContent();
        })
        .catch(error => {
            console.error(error);
            window.alert("Failed to load data");
            hideLoading();
        });
}

const copyBtn = document.querySelector("#copy");
copyBtn.addEventListener("click", setClipboard);

async function setClipboard() {
    const data = getCurrentlySelectedData();
    printDataToClipboard(data);
}

async function printDataToClipboard(data) {
    await navigator.clipboard.writeText(data);
}

const insertBtn = document.querySelector("#insert");
insertBtn.addEventListener("click", function () {
    const data = getCurrentlySelectedData();
    executeScript(insertData, data);
});

async function insertData(data) {
    try {
        var input = document.activeElement;
        if (input.tagName == "INPUT" || input.tagName == "TEXTAREA") {
            input.value = data;
        }
    } catch (error) {
        console.log("No input element found");
    }
}

async function executeScript(func, data) {
    if (data === undefined) {
        chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
            chrome.scripting.executeScript({
                target: { tabId: tabs[0].id },
                function: func
            });
        });
    }
    else {
        chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
            chrome.scripting.executeScript({
                target: { tabId: tabs[0].id },
                function: func,
                args: [data]
            });
        });
    }
}

function getCurrentlySelectedData() {
    const select = document.getElementById("select_field")
    const selectedValue = select.value;
    console.log(person);
    switch (selectedValue) {
        case "name":
            return person.name;
        case "date_of_birth":
            return person.date_of_birth;
        case "document_nr":
            return person.document_nr;
        default:
            return "";
    }
}

function actualizeData() {
    const name = document.getElementById("name");
    name.textContent = person.name;
}

function hideContent() {
    const content = document.getElementById("content");
    content.style.display = "none";
}

function showContent() {
    const content = document.getElementById("content");
    content.style.display = "flex";
}

function hideLoading() {
    const loading = document.getElementById("loading");
    loading.style.display = "none";
}

function showLoading() {
    const loading = document.getElementById("loading");
    loading.style.display = "block";
}