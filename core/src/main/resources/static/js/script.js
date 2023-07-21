// Js function test
function sendEmailByForm(path, email) {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = path;

    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'email';
    input.value = encodeURIComponent(email);
    form.appendChild(input);

    document.body.appendChild(form);
    form.submit();
}
