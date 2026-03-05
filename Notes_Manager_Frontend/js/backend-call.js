"use strict"
/*import {loadPage} from "/helpers/render-templates.js";
import {captureFormData} from "/helpers/retrieve-data.js";*/


const BASE_URL = "/notes"
const mainContainer = document.getElementById('page-content');


async function renderNotes() {
    mainContainer.innerHTML = "";
    await fetch(BASE_URL).then(res => res.json()).then(notes => {
        for (const note of notes) {
            let card = document.createElement('div');
            card.className = 'note-card';

            let title = document.createElement('h3');
            title.textContent = note.title;

            let content = document.createElement('h4');
            content.textContent = note.content;

            let date = document.createElement('p');
            date.textContent = "Last modified: " + note.lastModified;

            let tag = document.createElement("p");
            tag.textContent = "Category: " + note.tag;

            let editBtn = document.createElement('button');
            editBtn.textContent = 'Edit';
            editBtn.id = 'edit-note-btn';
            editBtn.dataset.id = note.id;

            let deleteBtn = document.createElement('button');
            deleteBtn.textContent = 'Delete';
            deleteBtn.id = 'delete-note-btn';
            deleteBtn.dataset.id = note.id;

            let archiveBtn = document.createElement('button');
            archiveBtn.textContent = 'Archive';
            archiveBtn.id = 'archive-note-btn';
            archiveBtn.dataset.id = note.id;

            card.appendChild(title);
            card.appendChild(content);
            card.appendChild(date);
            card.appendChild(tag);

            card.appendChild(editBtn);
            card.appendChild(deleteBtn);
            card.appendChild(archiveBtn);

            mainContainer.appendChild(card);
        }
    })

    
    let addButton = document.createElement("button");
    addButton.textContent = "+";
    addButton.id = "add-note-btn";
    addButton.title = "Add new note";
    mainContainer.appendChild(addButton);
}

async function renderArchivedNotes() {
    await fetch(`${BASE_URL}/archive`).then(res => res.json()).then(notes => {
        for (const note of notes) { //Note assemble and showing
            let card = document.createElement('div');
            card.className = 'note-card';
            
            let title = document.createElement('h4');
            title.textContent = note.title;
            
            let content = document.createElement('p');
            content.textContent = note.content;
            
            let date = document.createElement('p');
            date.textContent = "Last modified: " + note.lastModified;
            
            let tag = document.createElement("p");
            tag.textContent = note.tag;

            let deleteBtn = document.createElement('button'); //Delete button for archived notes
            deleteBtn.textContent = 'Delete';
            deleteBtn.id = 'delete-note-btn';
            deleteBtn.dataset.id = note.id;
            

            let restoreBtn = document.createElement('button');
            restoreBtn.textContent = 'Restore';
            restoreBtn.id = 'restore-note-btn';
            restoreBtn.dataset.id = note.id;
            deleteBtn.dataset.id = note.id;

            card.appendChild(title);
            card.appendChild(content);
            card.appendChild(date);
            card.appendChild(tag);
            card.appendChild(deleteBtn);
            card.appendChild(restoreBtn);
            mainContainer.appendChild(card);
        }
    })


}




//CRUD Operations

function createNote() {
    document.getElementById("cancel-btn").addEventListener("click", () => { window.location.reload(); })

    const form = document.getElementById("create-note-form");

    form.addEventListener("submit", async (e) => {
        let reqBody = captureFormData(e, form);

        const response = await fetch(`${BASE_URL}/new`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(reqBody)
        });

        if (response.ok) { window.location.reload(); }
    });
}

async function editNote(id) {
    const response = await fetch(`${BASE_URL}/${id}`);
    let note = await response.json();

    const title = document.getElementById("title");
    const content = document.getElementById("content");
    const category = document.getElementById("category");

    title.value = note.title;
    content.value = note.content;
    category.value = note.tag;

    document.getElementById("cancel-btn").addEventListener("click", () => { window.location.reload(); })

    const form = document.getElementById("create-note-form");
    form.addEventListener("submit", async (e) => {
        let reqBody = captureFormData(e, form);
        const response = await fetch(`${BASE_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(reqBody)
        });

        if (response.ok) { window.location.reload(); }
    })


}

async function eraseNote(id) {
    let response = await fetch(`${BASE_URL}/${id}`, { method: "DELETE" })
    if (response.ok) {
        console.log("Note successfully deleted");
        window.location.reload();
    }
}

async function archiveNote(id) {

    let promise = await fetch(`${BASE_URL}/archive/${id}`, { method: "PATCH" });
    if (promise.ok) {
        window.location.reload();
    }
}

async function restoreNote(id) {
    let promise = await fetch(`${BASE_URL}/restore/${id}`, { method: "PATCH" });
    if (promise.ok) {
        navigateTo("home");
    }
}


//Helpers
async function loadPage(pageName) {
    mainContainer.innerHTML = "";
    try {
        const response = await fetch(`templates/${pageName}.html`);
        const htmlContent = await response.text();
        mainContainer.innerHTML = htmlContent;
    } catch (error) {
        console.error('An error occurred during loading, please try refreshing', error);
    }
}

function captureFormData(e, form) {
    e.preventDefault();
    const fd = new FormData(form);
    const data = {
        title: fd.get("title"),
        content: fd.get("content"),
        tag: fd.get("category"),
    }
    return data;
}