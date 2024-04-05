export function highlightSelected() {
    try {
        var input = document.activeElement;
        if (input.tagName == "INPUT" || input.tagName == "TEXTAREA") {
            input.style.transition = 'transform 0.3s ease-in-out';
            input.style.position = 'absolute';
            input.style.top = '50%';
            input.style.left = '50%';
            input.style.right = 'auto';
            input.style.bottom = 'auto';
            input.style.marginRight = '-50%';
            input.style.transform = 'translate(-50%, -50%) scale(1.5)';
            input.style.boxShadow = '0 0 10px #F2BB3E';
        }
    } catch (error) {
        console.log("No input element found");
    }
}

export function removeHighlight() {
    // Select all input and textarea elements
    var inputsAndTextareas = document.querySelectorAll('input, textarea');

    // Add the event listener to each input field
    inputsAndTextareas.forEach(element => {
        element.style.transform = '';
        element.style.transition = '';
        element.style.position = '';
        element.style.top = '';
        element.style.left = '';
        element.style.right = '';
        element.style.bottom = '';
        element.style.marginRight = '';
        element.style.boxShadow = '';
    });
}