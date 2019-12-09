/* global $*/
$("#header").load("header.html");

$(function () {
    let contractDropdown = $("#contractDropdown");
    let draftNav = $("#draft_nav");
    let finalizeNav = $("#finalize_nav");
    let counterSignNav = $("#counter_sign_nav");
    let reviewNav = $("#review_nav");
    let signNav = $("#sign_nav");
});

function addBadge(nav) {
    let badge = document.createElement("span");
    badge.className = "badge badge-pill badge-danger";
    badge.textContent = "!";
    nav.append(badge);
}