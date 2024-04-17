const url = "http://localhost:80/users/1/profiles";

export async function fetchData() {
    const response = await fetch(url);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    const data = await response.json();
    return data;
}