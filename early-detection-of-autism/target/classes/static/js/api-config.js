// Use Spring Boot directly when the frontend is opened through Live Server.
const API_BASE_URL = window.location.port === '5500' ? 'http://localhost:8080' : '';

function apiUrl(path) {
    return `${API_BASE_URL}${path}`;
}
