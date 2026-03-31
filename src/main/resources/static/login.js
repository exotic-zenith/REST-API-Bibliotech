const statusEl = document.getElementById("status");
const TOKEN_KEY = "bibliotech_jwt";

function setStatus(message, isError = false) {
    statusEl.textContent = message;
    statusEl.style.color = isError ? "#b91c1c" : "#166534";
}

async function loginAdmin(event) {
    event.preventDefault();

    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("/api/v1/auth/token", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password })
        });

        if (!response.ok) {
            setStatus("Echec de connexion admin.", true);
            return;
        }

        const data = await response.json();
        if (!data.token) {
            setStatus("Token non recu.", true);
            return;
        }

        localStorage.setItem(TOKEN_KEY, data.token);
        window.location.replace("/manage.html");
    } catch (error) {
        setStatus("Impossible de generer le token.", true);
    }
}

document.getElementById("login-form").addEventListener("submit", loginAdmin);

