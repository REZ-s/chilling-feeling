function createFooter(path) {
    const footerSection = document.getElementById("footer");
    const bottomHomeButtons = [
        {href: '/', label: '홈', iconSrc: '/images/ic_home_de_24px.png'},
        {href: '/category', label: '카테고리', iconSrc: '/images/ic_category_de_24px.png'},
        {href: '/ready', label: '피드', iconSrc: '/images/ic_community_de_24px.png'},
        {href: '/me', label: '마이', iconSrc: '/images/ic_mypage_de_24px.png'}
    ];

    bottomHomeButtons.forEach(function(button) {
        if (path === button.href) {
            button.iconSrc = button.iconSrc.replace('de', 'ac');
        }
    });

    const bottomHomeDiv = document.createElement('div');
    bottomHomeDiv.classList.add('bottom-home');

    bottomHomeButtons.forEach(buttonInfo => {
        const button = document.createElement('button');
        button.classList.add('bottom-home-action');
        button.onclick = () => location.href = buttonInfo.href;

        const label = document.createElement('div');
        label.classList.add('bottom-label');
        label.textContent = buttonInfo.label;

        const img = document.createElement('img');
        img.classList.add('ic_bottom_icon');
        img.src = buttonInfo.iconSrc;

        button.appendChild(img);
        button.appendChild(label);
        bottomHomeDiv.appendChild(button);
    });

    footerSection.appendChild(bottomHomeDiv);
}
