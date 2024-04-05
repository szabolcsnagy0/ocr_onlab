import { fetchData } from "./api.js";
import { person } from "./person.js";

document.addEventListener("DOMContentLoaded", reloadData);

const reloadBtn = document.querySelector("#reload");
reloadBtn.addEventListener("click", reloadData);

async function reloadData() {
    fetchData()
        .then(data => {
            person.setData(data);
            actualizeData();
        })
        .catch(error => {
            console.error(error);
        });
}

function actualizeData() {
    const name = document.getElementById("name");
    name.textContent = person.name;
}

const copyBtn = document.querySelector("#copy");
copyBtn.addEventListener("click", setClipboard);

async function setClipboard() {
    printDataToClipboard(person.name);
}

async function printDataToClipboard(data) {
    await navigator.clipboard.writeText(data);
}

const insertBtn = document.querySelector("#insert");
insertBtn.addEventListener("click", executeInsertScript);

async function insertData(data) {
    try {
        var input = document.activeElement;
        if (input.tagName == "INPUT" || input.tagName == "TEXTAREA") {
            input.value = data;
        }
    } catch (error) {
        console.log("No input element found");
    } {
    }
}

async function executeInsertScript() {
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        chrome.scripting.executeScript({
            target: { tabId: tabs[0].id },
            function: insertData,
            args: [person.name]
        });
    });
}