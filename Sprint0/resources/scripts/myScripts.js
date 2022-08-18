/* Side Navbar */
function loadNav() {
    if(sessionStorage.getItem("sideNavOpen") === 'true') {
        openNav(false);
    } else {
        closeNav(false);
    }
}

function openNav(transition) {
    if(transition) {
        document.getElementById("sidenav").style.transition = "0.5s";
        document.getElementById("main").style.transition = "margin-left 0.5s";
    } else {
        document.getElementById("sidenav").style.transition = "none";
        document.getElementById("main").style.transition = "none";
    }
    document.getElementById("sidenav").style.width = "250px";
    document.getElementById("main").style.marginLeft = "250px";
    
    sessionStorage.setItem("sideNavOpen", true);
}

function closeNav(transition) {
    if(transition) {
        document.getElementById("sidenav").style.transition = "0.5s";
        document.getElementById("main").style.transition = "margin-left 0.5s";
    } else {
        document.getElementById("sidenav").style.transition = "none";
        document.getElementById("main").style.transition = "none";
    }
    document.getElementById("sidenav").style.width = "0";
    document.getElementById("main").style.marginLeft= "0";

    sessionStorage.setItem("sideNavOpen", false);
}