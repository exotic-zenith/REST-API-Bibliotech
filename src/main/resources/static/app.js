const statusEl = document.getElementById("status");
const booksOutput = document.getElementById("books-output");
const TOKEN_KEY = "bibliotech_jwt";

function setStatus(message, isError = false) {
    statusEl.textContent = message;
    statusEl.style.color = isError ? "#b91c1c" : "#166534";
}

function getToken() {
    return localStorage.getItem(TOKEN_KEY);
}

function redirectToLogin() {
    window.location.replace("/login.html");
}

function authHeaders() {
    const token = getToken();
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    };
}

function handleUnauthorized(response) {
    if (response.status === 401 || response.status === 403) {
        localStorage.removeItem(TOKEN_KEY);
        redirectToLogin();
        return true;
    }
    return false;
}

async function loadBooks() {
    try {
        const response = await fetch("/api/v1/books");
        const data = await response.json();
        const books = data.content || [];

        if (books.length === 0) {
            booksOutput.textContent = "Aucun livre disponible pour le moment.";
            return;
        }

        booksOutput.textContent = books.map((b) => {
            return `ID=${b.id} | ISBN=${b.isbn} | Titre=${b.title} | Stock=${b.stockDisponible}`;
        }).join("\n");
    } catch (error) {
        setStatus("Erreur lors du chargement des livres.", true);
    }
}

async function createBook(event) {
    event.preventDefault();

    const isbn = document.getElementById("isbn").value.trim();
    const title = document.getElementById("title").value.trim();
    const stockDisponible = Number(document.getElementById("stock").value);
    const authorName = document.getElementById("author").value.trim();
    const categoriesRaw = document.getElementById("categories").value.trim();

    const categories = categoriesRaw
        ? categoriesRaw.split(",").map((c) => c.trim()).filter((c) => c.length > 0)
        : [];

    const payload = {
        isbn,
        title,
        stockDisponible,
        authorName: authorName || null,
        categories: categories.length > 0 ? categories : null
    };

    try {
        const response = await fetch("/api/v1/books", {
            method: "POST",
            headers: authHeaders(),
            body: JSON.stringify(payload)
        });

        if (handleUnauthorized(response)) {
            return;
        }

        if (!response.ok) {
            const err = await response.json().catch(() => null);
            setStatus(err?.message || "Creation echouee.", true);
            return;
        }

        setStatus("Livre cree avec succes.");
        event.target.reset();
        document.getElementById("stock").value = "1";
        await loadBooks();
    } catch (error) {
        setStatus("Erreur reseau pendant la creation.", true);
    }
}

async function deleteBook(event) {
    event.preventDefault();
    const id = document.getElementById("delete-id").value;

    try {
        const response = await fetch(`/api/v1/books/${id}`, {
            method: "DELETE",
            headers: authHeaders()
        });

        if (handleUnauthorized(response)) {
            return;
        }

        if (!response.ok) {
            const err = await response.json().catch(() => null);
            setStatus(err?.message || "Suppression echouee.", true);
            return;
        }

        setStatus("Livre supprime avec succes.");
        event.target.reset();
        await loadBooks();
    } catch (error) {
        setStatus("Erreur reseau pendant la suppression.", true);
    }
}

function logout() {
    localStorage.removeItem(TOKEN_KEY);
    redirectToLogin();
}

if (!getToken()) {
    redirectToLogin();
} else {
    document.getElementById("refresh-books").addEventListener("click", loadBooks);
    document.getElementById("create-book-form").addEventListener("submit", createBook);
    document.getElementById("delete-book-form").addEventListener("submit", deleteBook);
    document.getElementById("logout-btn").addEventListener("click", logout);
    loadBooks();
}
