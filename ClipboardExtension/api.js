const base_url = "http://localhost:80";
// const url = "https://identity-application.azurewebsites.net/";

export async function login(email, password) {
    const response = await fetch(`${base_url}/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.text();

    localStorage.setItem('jwt', data);

    return data;
}

async function fetchWithToken(url, method, body) {
    const token = localStorage.getItem('jwt');
    if (!token) {
        throw new Error('No token saved!');
    }

    const options = {
        method: method,
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(`${base_url}${url}`, options);

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    if (response.headers.get('content-length') === '0' || !response.headers.get('content-type').includes('application/json')) {
        return null;
    }

    return await response.json();
}

export async function testToken() {
    try {
        await fetchWithToken('/auth/token', 'GET');
        return true;
    } catch (error) {
        return false;
    }
}

export async function fetchProfiles() {
    return await fetchWithToken('/user/profiles/list', 'GET');
}

export async function logout() {
    localStorage.removeItem('jwt');
}
