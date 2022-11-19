const navLink = document.querySelector("#navLink");
const navP = document.querySelector("#navP");
const dataDiv = document.querySelector("#data");
const logo = document.querySelector("#logo");

async function login(){
    return await fetch('http://localhost:8080/api')
    .then(res => res.json())
    .then(data => {
        navP.appendChild(document.createTextNode(data.login))
        logo.src = data.avatar_url;
        navLink.classList.add('hidden');
        navP.classList.remove('hidden');
        return data.login
    })
    .catch(err => {
        logo.src = "./public/avatar-g7811f95d3_640.png";
        navP.classList.add('hidden');
        navLink.classList.remove('hidden');
    })
}

function fetchData(userName){
    fetch(`http://localhost:8080/api/${userName}`)
    .then(res => res.json())
    .then(data => {
        let div = document.createElement('div');
        div.classList.add("repoContainer");
        let p = document.createElement('p');
        p.innerHTML = "REPOS";

        div.appendChild(p);

        data.forEach(element => {
            let tempP = document.createElement('p');
            tempP.innerHTML = element.name;
            div.appendChild(tempP);
        });

        dataDiv.replaceChildren(div);
    })
    .catch(err => {
        var img = document.createElement('img');
        img.src = './public/github-g48c737156_1280.jpg'
        dataDiv.replaceChildren(img);
    })
}

window.addEventListener('load', (e) => {
    login()
    .then((userName) => fetchData(userName));
})
