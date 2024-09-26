import { fetchProfiles, logout } from "./api.js";
import { NationalId } from "./entitites/national-id.js";
import { Profile } from "./entitites/profile.js";
import { highlightSelected } from "./highlight.js";
import { removeHighlight } from "./highlight.js";
import { Document } from "./entitites/document.js";


document.addEventListener("DOMContentLoaded", function () {
    reloadData();
    executeScript(highlightSelected);
});

document.addEventListener("visibilitychange", function () {
    executeScript(removeHighlight)
});

// const reloadBtn = document.querySelector("#reload");
// reloadBtn.addEventListener("click", reloadData);

async function reloadData() {
    hideContent();
    showLoading();
    fetchProfiles()
        .then(data => {
            Profile.setProfileList(data);
            actualizeProfileSelect();
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

document.getElementById("logout").addEventListener("click", function () {
    logout();
    window.location.href = '../login/login.html';
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
    chrome.tabs.query({ active: true, currentWindow: true }, function (tabs) {
        var url = tabs[0].url;
        if (url !== undefined && !url.startsWith('chrome://')) {
            if (data === undefined) {
                chrome.scripting.executeScript({
                    target: { tabId: tabs[0].id },
                    function: func
                });
            }
            else {
                chrome.scripting.executeScript({
                    target: { tabId: tabs[0].id },
                    function: func,
                    args: [data]
                });
            }
        }
    });
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


var selectProfile = document.getElementById("select_profile");
var selectDocument = document.getElementById("select_document");
var selectField = document.getElementById("select_field");

function getCurrentlySelectedData() {
    const selectedProfile = Profile.profileList.find(profile => profile.id == selectProfile.value);
    const selectedNationalId = selectedProfile.nationalIds.find(nationalId => nationalId.documentNr == selectDocument.value);
    if(selectedNationalId !== undefined) {
        return selectedNationalId[selectField.value];
    }
    const selectedDocument = selectedProfile.documents.find(doc => doc.id == selectDocument.value);
    return selectedDocument.fieldsList.find(field => field.title == selectField.value).value;
}

function actualizeProfileSelect() {
    for (var i = 0; i < Profile.profileList.length; i++) {
        var option = document.createElement("option");
        option.value = Profile.profileList[i].id;
        option.text = Profile.profileList[i].name;
        selectProfile.appendChild(option);
    }
}

function actualizeDocumentSelect(selectedProfile) {
    if(selectedProfile === undefined || (selectedProfile.nationalIds === undefined && selectedProfile.documents == undefined)) {
        return;
    }
    selectDocument.innerHTML = '';
    for (var i = 0; i < selectedProfile.nationalIds.length; i++) {
        var option = document.createElement("option");
        option.value = selectedProfile.nationalIds[i].documentNr;
        option.text = "HU National Id - " + selectedProfile.nationalIds[i].documentNr;
        selectDocument.appendChild(option);
    }
    for (var i = 0; i < selectedProfile.documents.length; i++) {
        var option = document.createElement("option");
        option.value = selectedProfile.documents[i].id;
        option.text = selectedProfile.documents[i].name;
        selectDocument.appendChild(option);
    }
    actualizeFieldSelect();
}


// Add an event listener to the select_profile dropdown
selectProfile.addEventListener('change', function () {
    var selectedProfile = Profile.profileList.find(profile => profile.id == selectProfile.value);

    if (selectedProfile === undefined) {
        return;
    }

    actualizeDocumentSelect(selectedProfile);
    actualizeFieldSelect();
});

selectDocument.addEventListener('change', actualizeFieldSelect);

function actualizeFieldSelect() {
    var selectedProfile = Profile.profileList.find(profile => profile.id == selectProfile.value);
    var selectedDocument = selectedProfile.nationalIds.find(nationalId => nationalId.documentNr == selectDocument.value);

    if(selectedDocument === undefined) {
        selectedDocument = selectedProfile.documents.find(document => document.id = selectDocument.value);
    }

    selectField.innerHTML = '';

    if (selectedDocument === undefined) {
        return;
    }

    if (selectedDocument instanceof NationalId) {
        var options = [
            { value: "name", text: "Family name and Given name" },
            { value: "sex", text: "Sex" },
            { value: "nationality", text: "Nationality" },
            { value: "dateOfBirth", text: "Date of birth" },
            { value: "dateOfExpiry", text: "Date of expiry" },
            { value: "documentNr", text: "Document number" },
            { value: "can", text: "CAN" },
            { value: "placeOfBirth", text: "Place of birth" },
            { value: "nameAtBirth", text: "Name at birth" },
            { value: "mothersName", text: "Mother's name" },
            { value: "authority", text: "Authority" }
        ];
        options.forEach(function (option) {
            if (selectedDocument[option.value]) {
                var opt = document.createElement("option");
                opt.value = option.value;
                opt.text = option.text;
                selectField.appendChild(opt);
            }
        })
    } else {
        selectedDocument.fieldsList.forEach(function (field) {
            var opt = document.createElement("option");
            opt.value = field.title;
            opt.text = field.title.replace(/:/g, '');            ;
            selectField.append(opt); 
        })
    }
}