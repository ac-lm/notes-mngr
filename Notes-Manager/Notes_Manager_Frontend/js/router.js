
async function navigateTo(page, id) {
    
    if (page === "home") {
        await renderNotes();
    }

    if (page === "archive") {
        await loadPage("archive");
        await renderArchivedNotes();
    }

    if (page === "create") {
        await loadPage("create-note");
        createNote();
    }

    if (page === "edit") {
        await loadPage("create-note");
        editNote(id);
    }

}


document.addEventListener("click", (e) => {
    if (e.target.id === "nav-home") {
        e.preventDefault();
        navigateTo("home");
    }

    if (e.target.id === "nav-archive") {
        e.preventDefault();
        navigateTo("archive");
    }

    if (e.target.id === "add-note-btn") {
        e.preventDefault();
        navigateTo("create");
    }

    if (e.target.id === "edit-note-btn") {
        e.preventDefault();
        let noteId = e.target.dataset.id;
        navigateTo("edit", noteId);
    }

    if (e.target.id === "delete-note-btn") {
        e.preventDefault();
        let noteId = e.target.dataset.id;
        eraseNote(noteId);
    }

    if (e.target.id === "archive-note-btn") {
        e.preventDefault();
        let noteId = e.target.dataset.id;
        archiveNote(noteId);
    }

    if (e.target.id === "restore-note-btn") {
        e.preventDefault();
        let noteId = e.target.dataset.id;
        restoreNote(noteId);
    }

});

document.addEventListener("DOMContentLoaded", () => {
    navigateTo("home");
});