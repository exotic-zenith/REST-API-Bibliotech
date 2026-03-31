const statusEl = document.getElementById("status");
const booksOutput = document.getElementById("books-output");
const tokenInput = document.getElementById("token");

function setStatus(message, isError = false) {
    statusEl.textContent = message;
    statusEl.style.color = isError ? "#b91c1c" : "#166534";
}

function authHeaders() {
    const token = tokenInput.value.trim();
    if (!token) {
        return { "Content-Type": "application/json" };
    }
    return {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    };
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
        tokenInput.value = data.token || "";
        setStatus("Token JWT genere avec succes.");
    } catch (error) {
        setStatus("Impossible de generer le token.", true);
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

        if (!response.ok) {
            const err = await response.json().catch(() => null);
            setStatus(err?.message || "Creation echouee (verifiez le token admin / donnees).", true);
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

        if (!response.ok) {
            const err = await response.json().catch(() => null);
            setStatus(err?.message || "Suppression echouee (verifiez token admin / ID).", true);
            return;
        }

        setStatus("Livre supprime avec succes.");
        event.target.reset();
        await loadBooks();
    } catch (error) {
        setStatus("Erreur reseau pendant la suppression.", true);
    }
}

document.getElementById("login-form").addEventListener("submit", loginAdmin);
document.getElementById("refresh-books").addEventListener("click", loadBooks);
document.getElementById("create-book-form").addEventListener("submit", createBook);
document.getElementById("delete-book-form").addEventListener("submit", deleteBook);

loadBooks();

