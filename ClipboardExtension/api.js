export { fetchData };
import { Person } from "./person.js";

const url = "http://localhost:8080/selected/data";

async function fetchData() {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return new Person(data);
}