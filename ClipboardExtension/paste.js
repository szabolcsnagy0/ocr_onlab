url = "http://localhost:8080/text"

var input = document.activeElement;
if (input.tagName == "INPUT" || input.tagName == "TEXTAREA") {
    getData(url).then(data => input.value = data.text).catch(error => console.error(error));
}

async function getData(url) {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
}