/***
 * Bottom Menu Container
 */
function createFooter() {
    const footerSection = document.getElementById("footer");
    const bottomHomeButtons = [
        {href: '/', label: '홈', iconSrc: '/images/ic_home_de_24px.png'},
        {href: '/category', label: '카테고리', iconSrc: '/images/ic_category_de_24px.png'},
        {href: '/ready', label: '피드', iconSrc: '/images/ic_community_de_24px.png'},
        {href: '/me', label: '마이', iconSrc: '/images/ic_mypage_de_24px.png'}
    ];

    bottomHomeButtons.forEach(function (button) {
        if (document.location.pathname === button.href || document.location.href === button.href) {
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

/***
 * Item List Container Using Category, Search
 * @param parentElement
 */
function createItemListForCategory(parentElement, products) {
    if (parentElement == null) {
        return;
    }

    const wrapCategoryContainer = document.createElement("div");
    wrapCategoryContainer.setAttribute("class", "wrap-category-container");
    wrapCategoryContainer.setAttribute("id", "yesDataFrame");

    const wrapCountingRange = document.createElement("div");
    wrapCountingRange.setAttribute("class", "wrap-counting-range");

    const countingProductText = document.createElement("div");
    countingProductText.setAttribute("class", "counting-product-text");
    countingProductText.innerText = "활성화된 검색 결과 : " + products.length + "개";
    wrapCountingRange.appendChild(countingProductText);

    const wrapRangeChevron = document.createElement('div');
    wrapRangeChevron.classList.add('wrap-range-chevron');

    const button = document.createElement('button');
    button.classList.add('category-dropdown-btn');

    const currentOption = document.createElement('a');
    currentOption.id = 'currentOption';
    currentOption.classList.add('range-label');
    currentOption.textContent = '신상순';

    const dropdownIcon = document.createElement('img');
    dropdownIcon.src = '/images/ic_chevron_down_10px.png';
    dropdownIcon.classList.add('ic_chevron_down_10px');

    button.appendChild(currentOption);
    button.appendChild(dropdownIcon);

    const dropdownContent = document.createElement('div');
    dropdownContent.classList.add('category-dropdown-content');

    const option1 = document.createElement('a');
    option1.id = 'option1';
    option1.classList.add('range-label');
    option1.textContent = '오래된순';

    const option2 = document.createElement('a');
    option2.id = 'option2';
    option2.classList.add('range-label');
    option2.textContent = '인기순';

    dropdownContent.appendChild(option1);
    dropdownContent.appendChild(option2);

    wrapRangeChevron.appendChild(button);
    wrapRangeChevron.appendChild(dropdownContent);

    wrapCountingRange.appendChild(wrapRangeChevron);
    wrapCategoryContainer.appendChild(wrapCountingRange);

    createItemList(wrapCategoryContainer, products);

    parentElement.appendChild(wrapCategoryContainer);

    const wrapCountingRangeEmpty = document.createElement("div");
    wrapCountingRangeEmpty.className = "wrap-counting-range";
    wrapCountingRangeEmpty.id = "surplusEmptySpace";

    parentElement.appendChild(wrapCountingRangeEmpty);
}

function updateItemListLength(length) {
    let countingProductText = document.getElementsByClassName('counting-product-text')[0];
    countingProductText.innerText = "활성화된 검색 결과 : " + length + "개";
}

function createItemList(parentElement, products) {
    const wrapCategoryProductList = document.createElement("div");
    wrapCategoryProductList.setAttribute("class", "wrap-category-product-list");

    for (let i = 0; i < products.length; i += 2) {
        const wrapHorizontality = document.createElement("div");
        wrapHorizontality.setAttribute("class", "wrap-horizontality");

        createItemCard01(wrapHorizontality, products[i]);
        if (i + 1 < products.length) {
            createItemCard01(wrapHorizontality, products[i + 1]);
        }

        wrapCategoryProductList.appendChild(wrapHorizontality);
    }

    parentElement.appendChild(wrapCategoryProductList);
}

function createItemListForNext(parentElement, products) {
    if (products == null || products.length === 0) {
        return false;
    }

    for (let i = 0; i < products.length; i += 2) {
        const wrapHorizontality = document.createElement("div");
        wrapHorizontality.setAttribute("class", "wrap-horizontality");

        createItemCard01(wrapHorizontality, products[i]);
        if (i + 1 < products.length) {
            createItemCard01(wrapHorizontality, products[i + 1]);
        }

        parentElement.appendChild(wrapHorizontality);
    }

    return true;
}

/***
 * One Item Container. 01
 * @param parentElement
 * @param product
 */
function createItemCard01(parentElement, product) {
    if (parentElement == null) {
        return;
    }

    const wrapProductCard01 = document.createElement('div');
    wrapProductCard01.className = 'wrap-product-card01';

    const productCard01 = document.createElement('div');
    productCard01.className = 'product-card01';

    const productPhotoContainer = document.createElement('button');
    productPhotoContainer.className = 'product-list01-photo-container';
    productPhotoContainer.id = product.name;

    const photoContainerWrapImg = document.createElement('div');
    photoContainerWrapImg.className = 'photo-container-wrap-img';

    const image = document.createElement('img');
    image.src = product.imageUrl;   // (1) input item image
    image.className = 'recommend-image01';

    const buttonHeart = document.createElement('button');
    buttonHeart.className = 'btn-heart';

    const imageHeart = document.createElement('img');
    imageHeart.src = '/images/ic_heart_24px.png';
    imageHeart.className = 'ic_heart_24px';

    buttonHeart.appendChild(imageHeart);

    photoContainerWrapImg.appendChild(image);

    productPhotoContainer.appendChild(photoContainerWrapImg);
    productPhotoContainer.appendChild(buttonHeart);

    const productInfo = document.createElement('div');
    productInfo.className = 'product-list01-product-info';

    const productName = document.createElement('div');
    productName.className = 'product-list01-product-name';

    const productNameType = document.createElement('div');
    productNameType.className = 'product-list01-product-name-type';
    productNameType.textContent = product.type;   // (2) input item type

    const productNameName = document.createElement('div');
    productNameName.className = 'product-list01-product-name-name';
    productNameName.textContent = product.name;  // (3) input item name

    productName.appendChild(productNameType);
    productName.appendChild(productNameName);

    const caption = document.createElement('div');
    caption.className = 'product-list01-caption';

    const starScoreReview = document.createElement('div');
    starScoreReview.className = 'star-score-review';

    const imageStar = document.createElement('img');
    imageStar.src = '/images/ic_star_10px.png';
    imageStar.className = 'ic_star_10px';

    const score = document.createElement('div');
    score.className = 'score';

    const starRating = document.createElement('div');
    starRating.className = 'star-rating';
    starRating.textContent = product.score;  // (4) input item rating

    const reviews = document.createElement('div');
    reviews.className = 'reviews';
    reviews.textContent = '(' + product.reviewCount + ')'    // (5) input item reviews

    starScoreReview.appendChild(imageStar);

    score.appendChild(starRating);
    score.appendChild(reviews);

    starScoreReview.appendChild(score);

    caption.appendChild(starScoreReview);

    const wrapChip = document.createElement('div');
    wrapChip.className = 'wrap-chip';

    const productChip = document.createElement('div');
    productChip.className = 'product-chip';

    const chipLabel = document.createElement('div');
    chipLabel.className = 'chip-label';
    chipLabel.textContent = product.label;  // (6) input item chip label

    productChip.appendChild(chipLabel);

    wrapChip.appendChild(productChip);

    caption.appendChild(wrapChip);

    productInfo.appendChild(productName);
    productInfo.appendChild(caption);

    productCard01.appendChild(productPhotoContainer);
    productCard01.appendChild(productInfo);

    wrapProductCard01.appendChild(productCard01);

    parentElement.appendChild(wrapProductCard01);
}

/**
 * One Item Container. 02
 * @param parentElement
 * @param product
 */
function createItemCard02(parentElement, product) {
    if (parentElement == null) {
        return;
    }

    const productCardDiv = document.createElement('div');
    productCardDiv.classList.add('product-card02');

    const photoContainerDiv = document.createElement('button');
    photoContainerDiv.classList.add('product-list02-photo-container');
    photoContainerDiv.id = product.name;

    const image = document.createElement('img');
    image.src = product.imageUrl;
    image.classList.add('recommend-image02');

    photoContainerDiv.appendChild(image);

    const productInfoDiv = document.createElement('div');
    productInfoDiv.classList.add('product-list02-product-info');

    const nameStarDiv = document.createElement('div');
    nameStarDiv.classList.add('product-list02-name-star');

    const productNameDiv = document.createElement('div');
    productNameDiv.classList.add('product-list02-name');
    productNameDiv.innerText = product.name;

    const starGroupDiv = document.createElement('div');
    starGroupDiv.classList.add('star-group');
    starGroupDiv.innerHTML = createScoreImage(product.score);

    nameStarDiv.appendChild(productNameDiv);
    nameStarDiv.appendChild(starGroupDiv);

    const captionPriceDiv = document.createElement('div');
    captionPriceDiv.classList.add('product-list02-caption-price');

    const captionDiv = document.createElement('div');
    captionDiv.classList.add('product-list02-caption');
    captionDiv.innerHTML = product.summary;

    captionPriceDiv.appendChild(captionDiv);

    productInfoDiv.appendChild(nameStarDiv);
    productInfoDiv.appendChild(captionPriceDiv);

    productCardDiv.appendChild(photoContainerDiv);
    productCardDiv.appendChild(productInfoDiv);

    parentElement.appendChild(productCardDiv);
}

function updateItemCard02(parentElement, newProduct) {
    if (!parentElement) {
        return;
    }

    let productContainer = parentElement.querySelector('.product-list02-photo-container');
    if (productContainer) {
        productContainer.id = newProduct.name;
    }

    let image = parentElement.querySelector('.recommend-image02');
    if (image) {
        image.src = newProduct.imageUrl;
    }

    let productNameDiv = parentElement.querySelector('.product-list02-name');
    if (productNameDiv) {
        productNameDiv.innerText = newProduct.name;
    }

    let starGroupDiv = parentElement.querySelector('.star-group');
    if (starGroupDiv) {
        starGroupDiv.innerHTML = createScoreImage(newProduct.score);
    }

    let captionDiv = parentElement.querySelector('.product-list02-caption');
    if (captionDiv) {
        captionDiv.innerHTML = newProduct.summary;
    }
}

/***
 * No Item Container Using Category, Search
 * @param parentElement
 */
function createEmptyItemListForCategory(parentElement) {
    if (parentElement == null) {
        return;
    }

    const searchResultContentsFrameInner = document.createElement("div");
    searchResultContentsFrameInner.className = "search-result-contents-frame-inner";
    searchResultContentsFrameInner.id = "noDataFrame";

    const wrapIllustrationTextSearchNoData = document.createElement("div");
    wrapIllustrationTextSearchNoData.className = "wrap-illustration-text-search-no-data";

    const illustrationSearchNoData = document.createElement("div");
    illustrationSearchNoData.className = "illustration-search-no-data";

    const img = document.createElement("img");
    img.src = "/images/ic_x_face.png";

    const textSearchNoData = document.createElement("div");
    textSearchNoData.className = "text-search-no-data";

    const spanElement = document.createElement("span");
    spanElement.setAttribute("class", "text-style-1");
    spanElement.innerText = "";

    const textNode = document.createTextNode("검색 결과가 없습니다.");

    textSearchNoData.appendChild(spanElement);
    textSearchNoData.appendChild(textNode);

    illustrationSearchNoData.appendChild(img);
    wrapIllustrationTextSearchNoData.appendChild(illustrationSearchNoData);

    wrapIllustrationTextSearchNoData.appendChild(textSearchNoData);
    searchResultContentsFrameInner.appendChild(wrapIllustrationTextSearchNoData);

    parentElement.appendChild(searchResultContentsFrameInner);
}

/***
 * Display Item List Container (main)
 * Need arguments = { parentElement, goodsViewList, type : optional }
 */
function displayItemList() {
    if (arguments.length < 2) {
        return;
    }

    const parentElement = arguments[0];
    const goodsViewList = arguments[1];

    const products = getProducts(goodsViewList);
    for (let i = 0; i < goodsViewList.length; i++) {
        createItemCard01(parentElement, products[i]);
    }

    activeGoodsDetailsURL();
}

function displayItemListForCategory() {
    if (arguments.length < 2) {
        return;
    }

    const parentElement = arguments[0];
    const goodsViewList = arguments[1];

    if (arguments.length === 2) {
        createBaseItemListForCategory(parentElement, getProducts(goodsViewList));
    } else if (arguments.length === 3) {
        const type = arguments[2];

        if (type === "전체" || type === "All" || type === "all" || type === "ALL") {
            createBaseItemListForCategory(parentElement, getProducts(goodsViewList));
        } else {
            createBaseItemListForCategory(parentElement, getProductsByType(goodsViewList, type));
        }
    }

    activeGoodsDetailsURL();
}

function getProducts(goodsViewList) {
    return goodsViewList;
}

function getProductsByType(goodsViewList, type) {
    if (type === '전체' || type === 'all' || type === 'All' || type === 'ALL') {
        return goodsViewList;
    }

    let products = [];

    for (let i = 0; i < goodsViewList.length; i++) {
        if (goodsViewList[i].type === type) {
            products.push(goodsViewList[i]);
        }
    }

    return products;
}

function createBaseItemListForCategory(parentElement, products) {
    if (products == null || products.length === 0) {
        createEmptyItemListForCategory(parentElement);
    } else {
        createItemListForCategory(parentElement, products);
    }
}

function activeGoodsDetailsURL() {
    let goodsContainers01 = document.getElementsByClassName('product-list01-photo-container');
    let goodsContainers02 = document.getElementsByClassName('product-list02-photo-container');
    let goodsContainers03 = document.getElementsByClassName('photo-container-85px');

    for (let goodsContainer of goodsContainers01) {
        goodsContainer.addEventListener('click', function () {
            location.href = '/goods/' + goodsContainer.id.toString();
        });
    }

    for (let goodsContainer of goodsContainers02) {
        goodsContainer.addEventListener('click', function () {
            location.href = '/goods/' + goodsContainer.id.toString();
        });
    }

    for (let goodsContainer of goodsContainers03) {
        goodsContainer.addEventListener('click', function () {
            location.href = '/goods/' + goodsContainer.id.toString();
        });
    }
}

function applyPlusBtnElements() {
    plusBtnElements = document.querySelectorAll('[class*="ic_plus"]');
    plusBtnElements.forEach(function (plusBtn) {
        plusBtn.addEventListener('click', function () {
            let parentElement = plusBtn.parentElement;
            let goodsCountElement = parentElement.querySelector('[id*="goodsCount"]');
            let goodsCount = parseInt(goodsCountElement.innerText);

            goodsCountElement.innerText = (goodsCount + 1).toString();
        });
    });
}

function applyMinusBtnElements() {
    minusBtnElements = document.querySelectorAll('[class*="ic_minus"]');
    minusBtnElements.forEach(function (minusBtn) {
        minusBtn.addEventListener('click', function () {
            let parentElement = minusBtn.parentElement;
            let goodsCountElement = parentElement.querySelector('[id*="goodsCount"]');
            let goodsCount = parseInt(goodsCountElement.innerText);

            if (goodsCount > 1) {
                goodsCountElement.innerText = (goodsCount - 1).toString();
            }
        });
    });
}

function displayUsername(username) {
    document.getElementById('username').innerText = username;
}

async function displayUserCategories() {
    const categories = await getUserCategories();
    const keywords = categories.split(',');
    const wrapContainer = document.getElementById('keywords');

    for (let i = 0; i < Math.min(3, keywords.length); i++) {
        const keywordDiv = document.createElement('div');
        keywordDiv.classList.add('my-taste-keywords');
        const illustDiv = document.createElement('div');
        illustDiv.classList.add('my-taste-keyword-illust');
        const illustImg = document.createElement('img');
        illustImg.classList.add('illust_keyword_img');
        illustImg.src = getKeywordImage(keywords[i]);
        const labelDiv = document.createElement('div');
        labelDiv.classList.add('my-taste-keyword-label');
        labelDiv.innerText = keywords[i];

        illustDiv.appendChild(illustImg);
        keywordDiv.appendChild(illustDiv);
        keywordDiv.appendChild(labelDiv);

        wrapContainer.appendChild(keywordDiv);
    }
}

async function displayUserFeeling() {
    const userFeeling = await getUserFeeling();
    const wrapContainer = document.getElementById('keywords');

    const keywordDiv = document.createElement('div');
    keywordDiv.classList.add('my-taste-keywords');
    const illustDiv = document.createElement('div');
    illustDiv.classList.add('my-taste-keyword-illust');
    const illustImg = document.createElement('img');
    illustImg.classList.add('illust_keyword_img');
    illustImg.src = getKeywordImage(userFeeling);
    const labelDiv = document.createElement('div');
    labelDiv.classList.add('my-taste-keyword-label');
    labelDiv.innerText = getKeywordText(userFeeling);

    illustDiv.appendChild(illustImg);
    keywordDiv.appendChild(illustDiv);
    keywordDiv.appendChild(labelDiv);

    wrapContainer.appendChild(keywordDiv);
}

function getKeywordText(keyword) {
    let text = '잘모르겠어요';

    if (keyword === 'SMILE') {
        text = '즐거워요';
    } else if (keyword === 'HAPPY') {
        text = '기뻐요';
    } else if (keyword === 'SAD') {
        text = '슬퍼요';
    } else if (keyword === 'ANGRY') {
        text = '화나요';
    } else if (keyword === 'BLANK') {
        text = '잘모르겠어요';
    }

    return text;
}

function getKeywordImage(keyword) {
    let imgUrl = '/images/illust_face_feeling_normal_green.png';

    if (keyword === '와인') {
        imgUrl = '/images/illust_keyword_wine.png';
    } else if (keyword === '위스키') {
        imgUrl = '/images/illust_keyword_whisky.png';
    } else if (keyword === '칵테일') {
        imgUrl = '/images/illust_keyword_cocktail.png';
    } else if (keyword === '전통주') {
        imgUrl = '/images/illust_keyword_traditional.png';
    } else if (keyword === '육류') {
        imgUrl = '/images/illust_keyword_steak.png';
    } else if (keyword === '해산물') {
        imgUrl = '/images/illust_keyword_fish.png';
    } else if (keyword === '디저트') {
        imgUrl = '/images/illust_keyword_dessert.png';
    } else if (keyword === '인기상품') {
        imgUrl = '/images/illust_keyword_heart.png';
    } else if (keyword === '신상품') {
        imgUrl = '/images/illust_keyword_new.png';
    } else if (keyword === '최고상품') {
        imgUrl = '/images/illust_keyword_best.png';
    } else if (keyword === 'SMILE') {
        imgUrl = '/images/illust_face_feeling_fun_green.png';
    } else if (keyword === 'HAPPY') {
        imgUrl = '/images/illust_face_feeling_happy_orange.png';
    } else if (keyword === 'SAD') {
        imgUrl = '/images/illust_face_feeling_sad_blue.png';
    } else if (keyword === 'ANGRY') {
        imgUrl = '/images/illust_face_feeling_angry_red.png';
    } else if (keyword === 'BLANK') {
        imgUrl = '/images/illust_face_feeling_normal_green.png';
    }

    return imgUrl;
}

async function getUserCategories() {
    try {
        const response = await fetch('/api/v1/recommendation/base/keyword');
        if (response.ok) {
            return await response.text();
        }
        throw response;
    } catch (error) {
        throw error;
    }
}


async function getUserFeeling() {
    try {
        const response = await fetch('/api/v1/recommendation/daily/feeling');
        if (response.ok) {
            return await response.text();
        }
        throw response;
    } catch (error) {
        throw error;
    }
}

/**
 * Swiper Slide (need global variable 'startX', 'nowX', 'pressed')
 * To use : 'startX', 'nowX', 'pressed' declaration in script
 * @param productsFrame
 * @param innerSlider
 */
function setSwiperSlide(productsFrame, innerSlider) {
    updateGridTemplateColumns(innerSlider);

    /* PC Swipe (Mouse Drag) */
    productsFrame.addEventListener("mousedown", (e) => {
        pressed = true;
        startX = e.offsetX - innerSlider.offsetLeft;
        //productsFrame.style.cursor = "grabbing";
    });

    productsFrame.addEventListener("mouseenter", () => {
        pressed = false;
        //productsFrame.style.cursor = "grab";
    });

    productsFrame.addEventListener("mouseup", () => {
        pressed = false;
        //productsFrame.style.cursor = "grab";
    });

    productsFrame.addEventListener("mousemove", (e) => {
        if (!pressed) {
            return;
        }

        e.preventDefault();
        nowX = e.offsetX;

        innerSlider.style.left = `${nowX - startX}px`;
        checkBoundary();
    });

    window.addEventListener("mouseup", () => {
        pressed = false;
    });

    /* Mobile Swipe */
    productsFrame.addEventListener("touchstart", (e) => {
        pressed = true;
        startX = e.touches[0].clientX - innerSlider.offsetLeft;
    });

    productsFrame.addEventListener("touchend", () => {
        pressed = false;
    });

    productsFrame.addEventListener("touchmove", (e) => {
        if (!pressed) {
            return;
        }

        e.preventDefault();
        nowX = e.touches[0].clientX;

        innerSlider.style.left = `${nowX - startX}px`;
        checkBoundary();
    });

    function checkBoundary() {
        let outer = productsFrame.getBoundingClientRect();
        let inner = innerSlider.getBoundingClientRect();

        if (parseInt(innerSlider.style.left) > 0) {
            innerSlider.style.left = "0px";
        } else if (inner.right < outer.right) {
            innerSlider.style.left = `-${inner.width - outer.width}px`;
        }
    }

    function updateGridTemplateColumns(innerSlider) {
        const childCount = innerSlider.childElementCount;
        innerSlider.style.gridTemplateColumns = `repeat(${childCount}, 1fr)`;
    }
}

function isNullOrWhiteSpace(value) {
    return value === null || value.trim() === "";
}

function createScoreImage(score) {
    let scores = score.split(".");
    let scoreImages = "";
    let count = 0;

    const INumber = parseInt(scores[0]);
    const FNumber = parseInt(scores[1]);

    for (let i = 0; i < INumber; i++) {
        scoreImages += "<img src=\"/images/ic_star_10px.png\" class=\"ic_star_10px\">";
        count++;
    }

    if (FNumber > 7) {
        scoreImages += "<img src=\"/images/ic_star_10px.png\" class=\"ic_star_10px\">";
        count++;
    } else if (FNumber > 2) {
        scoreImages += "<img src=\"/images/ic_half_star_10px.png\" class=\"ic_half_star_10px\">";
        count++;
    }

    for (let i = count; i < 5; i++) {
        scoreImages += "<img src=\"/images/ic_star_off_10px.png\" class=\"ic_star_off_10px\">";
    }

    return scoreImages;
}

function createPaletteImage(paletteValue) {
    let paletteImage = null;

    switch (paletteValue) {
        case "0":
            paletteImage = "/images/wine_style_graphy_level1.png";
            break;
        case "1":
            paletteImage = "/images/wine_style_graphy_level2.png";
            break;
        case "2":
            paletteImage = "/images/wine_style_graphy_level3.png";
            break;
        case "3":
            paletteImage = "/images/wine_style_graphy_level4.png";
            break;
        case "4":
            paletteImage = "/images/wine_style_graphy_level5.png";
            break;
        default:
            break;
    }

    return paletteImage;
}

function fill(value) {
    if (value !== null && value !== undefined && value !== "") {
        return value;
    } else {
        return "-";
    }
}

function fillImage(value) {
    if (value !== null && value !== undefined && value !== "") {
        return `<img src="${value}">`;
    } else {
        return `<img src="/images/nothing.png">`;
    }
}

function createPaletteLabelFront(paletteName) {
    switch (paletteName) {
        case "바디":
            return "가벼움";
        case "탄닌":
            return "매끈함";
        case "당도":
            return "드라이";
        case "산도":
            return "낮음";
        case "탄산":
            return "없음";
        default:
            return "";
    }
}

function getPaletteEngName(paletteName) {
    switch (paletteName) {
        case "바디":
            return "Body";
        case "탄닌":
            return "Tannin";
        case "당도":
            return "Sweetness";
        case "산도":
            return "Acidity";
        case "탄산":
            return "Soda";
        default:
            return "";
    }
}

function createPaletteLabelBack(paletteName) {
    switch (paletteName) {
        case "바디":
            return "무거움";
        case "탄닌":
            return "떫음";
        case "당도":
            return "스위트";
        case "산도":
            return "시큼함";
        case "탄산":
            return "강함";
        default:
            return "";
    }
}

/***
 * 위시리스트(좋아요)에 상품 저장
 */
async function addWishList(goodsName) {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('유저 정보를 읽을 수 없습니다. \n로그인 페이지로 이동합니다.');
        location.href = "/login";
    }

    try {
        const response = await fetch('/api/v1/wishlist', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                goodsName
            })
        });

        if (response.ok) {
            const body = await response.text();
            if (body === 'success') {
                alert('위시리스트에 저장되었습니다.');
            }
        } else {
            throw response;
        }
    } catch (error) {
        alert('위시리스트 저장에 실패했습니다.');
    }
}

function applyWishListBtn(goodsName) {
    let heartBtn = document.getElementById('heartBtn');
    let heartImage = document.getElementById('heartImage');

    heartBtn.addEventListener('click', async function () {
        if (heartImage.src.toString().includes('/images/ic_heart_fill_24px.png')) {
            await removeWishList(goodsViewDetails.name);
        } else {
            await addWishList(goodsViewDetails.name);
        }

        await displayWishGoodsForGoodsPage(heartImage, goodsName);
    });
}

async function displayWishListGoods(parentElement, goodsViewList) {
    let elements = parentElement.querySelectorAll('[class*="ic_heart"]');

    let wishList = await getWishList();
    let wishListSet = new Set();
    for (let i = 0; i < wishList.length; i++) {
        wishListSet.add(wishList[i].goodsView.name);
    }

    let goodsViewSet = new Set();
    for (let i = 0; i < goodsViewList.length; i++) {
        goodsViewSet.add(goodsViewList[i].name);
    }

    for (let i = 0; i < goodsViewList.length; i++) {
        if (wishListSet.has(goodsViewList[i].name)) {
            elements[i].src = '/images/ic_heart_fill_24px.png';
        } else {
            elements[i].src = '/images/ic_heart_24px.png';
        }
    }
}

async function displayWishGoods(element, goodsName) {
    let isWishList = await checkWishListGoods(goodsName);
    if (isWishList === true) {
        element.src = '/images/ic_heart_fill_24px.png';
    } else {
        element.src = '/images/ic_heart_24px.png';
    }
}

async function displayWishGoodsForGoodsPage(element, goodsName) {
    let isWishList = await checkWishListGoods(goodsName);
    if (isWishList === true) {
        element.src = '/images/ic_heart_fill_24px.png';
    } else {
        element.src = '/images/ic_heart_black_24px.png';
    }
}

/**
 * 위시리스트에 있는 상품인지 확인
 */
async function checkWishListGoods(goodsName) {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        // 유저 정보를 읽을 수 없으니 위시리스트 요청 무시
        return false;
    }

    try {
        const response = await fetch('/api/v1/wishlist/checked', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                goodsName
            })
        });

        if (response.ok) {
            const body = await response.text();
            if (body === 'true') {
                return true;
            }
        } else {
            throw response;
        }
    } catch (error) {
        // 위시리스트에 없는 상품. 별다른 조치를 하지 않음
    }

    return false;
}

/***
 * 위시리스트 불러오기
 */
async function getWishList() {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('internal server error');
        return;
    }

    let wishListResponseList = [];

    // 비로그인 상태인 경우
    if (username == null || username.length === 0) {
        return wishListResponseList;
    }

    try {
        const response = await fetch('/api/v1/wishlist?username=' + username)

        if (response.ok) {
            wishListResponseList = response.json();
        } else {
            throw response;
        }
    } catch (error) {
        alert('위시리스트를 불러오는데 실패했습니다.');
    }

    return wishListResponseList;
}

/***
 * 위시리스트에서 상품 제거
 */
async function removeWishList(goodsName) {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('유저 정보를 읽을 수 없습니다. \n로그인 페이지로 이동합니다.');
        location.href = "/login";
    }

    try {
        const response = await fetch('/api/v1/wishlist', {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                goodsName
            })
        });

        if (response.ok) {
            const body = await response.text();
            if (body === 'success') {
                alert('위시리스트에서 제외했습니다.');
            }
        } else {
            throw response;
        }
    } catch (error) {
        alert('위시리스트에서 상품을 제외하는데 실패했습니다.');
    }
}

/***
 * 장바구니에 상품 저장
 */
async function addCart(goodsName, goodsCount) {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('유저 정보를 읽을 수 없습니다. \n로그인 페이지로 이동합니다.');
        location.href = "/login";
    }

    try {
        const response = await fetch('/api/v1/cart', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                goodsName,
                goodsCount
            })
        });

        if (response.ok) {
            const body = await response.text();
            if (body === 'success') {
                alert('장바구니에 저장되었습니다.');
                location.href = '/';
            }
        } else {
            throw response;
        }
    } catch (error) {
        alert('장바구니 저장에 실패했습니다.');
    }
}

/**
 * 장바구니 불러오기
 */
async function getCart() {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('유저 정보를 읽을 수 없습니다. \n로그인 페이지로 이동합니다.');
        location.href = "/login";
    }

    let cartResponseList;

    try {
        const response = await fetch('/api/v1/cart?username=' + username)

        if (response.ok) {
            cartResponseList = response.json();
        } else {
            throw response;
        }
    } catch (error) {
        alert('장바구니를 불러오는데 실패했습니다.');
    }

    return cartResponseList;
}

/**
 * 장바구니에서 상품 제거
 */
async function removeCart(goodsName) {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('유저 정보를 읽을 수 없습니다. \n로그인 페이지로 이동합니다.');
        location.href = "/login";
    }

    let goodsCount = 1;

    try {
        const response = await fetch('/api/v1/cart', {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                goodsName,
                goodsCount
            })
        });

        if (response.ok) {
            const body = await response.text();
            if (body === 'success') {
                // 상품 제거 성공
            }
        } else {
            throw response;
        }
    } catch (error) {
        alert('장바구니에서 상품을 제거하는데 실패했습니다.');
    }
}


/**
 * 주문하기
 */
async function ordersGoodsList(goodsName, goodsCount) {
    let username = '';

    try {
        const response = await fetch('/api/v1/user/authentication');
        if (response.ok) {
            username = await response.text();
        } else {
            throw response;
        }
    } catch (error) {
        alert('유저 정보를 읽을 수 없습니다. \n로그인 페이지로 이동합니다.');
        location.href = "/login";
    }

    try {
        const response = await fetch('/api/v1/order', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                username,
                goodsName,
                goodsCount
            })
        });

        if (response.ok) {
            const body = await response.text();
            if (body === 'success') {
                alert('주문이 완료되었습니다.');
                location.href = '/';
            }
        } else {
            throw response;
        }
    } catch (error) {
        alert('주문에 실패했습니다.');
    }
}

async function getBestSellerGoods(days) {
    let goodsViewDetails;
    let salesCount;

    await fetch('/api/v1/goods/best-seller/' + days)
        .then(response => response.json())
        .then(data => {
            goodsViewDetails = data.goodsViewDetails;
            salesCount = data.salesCount;
        })
        .catch(error => {
            console.error('베스트셀러 상품 가져오기 실패 : ', error);
        });

    return goodsViewDetails;
}

async function getPopularGoodsList(days) {
    let goodsViewList;

    await fetch('/api/v1/goods/popular/' + days)
        .then(response => response.json())
        .then(data => {
            goodsViewList = data;
        })
        .catch(error => {
            console.error('인기상품 가져오기 실패 : ', error);
        });

    return goodsViewList;
}

/***
 * Display Best Seller Item
 * arguments = { parentElement, days }
 */
async function displayBestSellerGoods(parentElement, days) {
    let goodsViewDetails = await getBestSellerGoods(days);
    if (goodsViewDetails == null || goodsViewDetails.length === 0) {
        removeAllChildNodes(parentElement);
        createEmptyItemListForCategory(parentElement);
        return;
    }

    removeAllChildNodes(parentElement);
    createItemCard02(parentElement, goodsViewDetails);
    activeGoodsDetailsURL();
}

function removeAllChildNodes(parentElement) {
    while (parentElement.firstChild) {
        parentElement.removeChild(parentElement.firstChild);
    }
}

async function getSearchResult(searchKeyword) {
    if (searchKeyword === '') {
        alert('검색어를 입력해주세요.');
        return;
    }

    await applyDeviceId();
    location.href = '/search/result?query=' + searchKeyword + '&deviceId=' + getDeviceId();
}

async function getUserActivityLogGoods(deviceId) {
    let goodsNameList;

    await fetch('/api/v1/recommendation/log/activity/goods?deviceId=' + deviceId)
        .then(response => response.json())
        .then(data => {
            goodsNameList = data;
        })
        .catch(error => {
            console.error('최근 검색어 가져오기 실패 : ', error);
        });

    return goodsNameList;
}

async function removeUserActivityLogGoods(deviceId, targetName) {
    try {
        const response = await fetch('/api/v1/recommendation/log/activity/goods', {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                deviceId,
                targetName
            })
        });

        if (response.ok) {
            return true;
        } else {
            throw response;
        }
    } catch (error) {
        alert('최근 검색어 삭제 실패');
        return false;
    }
}

async function createSearchHistoryButton(parentElement, text) {
    const btn = document.createElement('div');
    btn.className = 'btn-search-history';

    const label = document.createElement('div');
    label.className = 'btn-search-history-label';
    label.textContent = text;
    btn.appendChild(label);

    const bar = document.createElement('img');
    bar.src = '/images/bar.png';
    bar.className = 'btn-search-history-bar';
    btn.appendChild(bar);

    const close = document.createElement('img');
    close.src = '/images/ic_close_14px.png';
    close.className = 'ic_close_14px';
    btn.appendChild(close);

    parentElement.appendChild(btn);

    // close 버튼을 누르면, 최근 검색어를 삭제하도록 이벤트 등록
    close.addEventListener('click', async function () {
        let isPassed = await removeUserActivityLogGoods(getDeviceId(), text);
        if (isPassed) {
            close.parentElement.remove();
            let searchWords = document.getElementsByClassName('btn-search-history');
            if (searchWords == null || searchWords.length === 0) {
                createEmptySearchHistory(parentElement);
            }
        }
    });
}

function createEmptySearchHistory(parentElement) {
    const divElement = document.createElement("div");
    divElement.classList.add("recent-no-data-text");
    divElement.textContent = '최근 검색어가 없습니다.';

    parentElement.appendChild(divElement);
}

async function displaySearchHistoryBtn(parentElement) {
    await applyDeviceId();

    let goodsNameList = await getUserActivityLogGoods(getDeviceId());
    if (goodsNameList == null || goodsNameList.length === 0) {
        createEmptySearchHistory(parentElement);
        return;
    }

    for (let i = 0; i < goodsNameList.length; i++) {
        await createSearchHistoryButton(parentElement, goodsNameList[i]);
    }
}

function getDeviceId() {
    return localStorage.getItem('deviceId');
}

function saveDeviceId(deviceId) {
    localStorage.setItem('deviceId', deviceId);
}

async function createDeviceId() {
    let deviceId;

    await fetch('/api/v1/device/id')
        .then(response => response.json())
        .then(data => {
            deviceId = data;
        })
        .catch(error => {
            console.error('Failed to get device id : ', error);
        });

    return deviceId;
}

async function applyDeviceId() {
    if (getDeviceId() == null) {
        let deviceId = await createDeviceId();
        saveDeviceId(deviceId);
    }
}

async function getRandomGoods() {
    let goodsViewDetails;

    await fetch('/api/v1/goods/random')
        .then(response => response.json())
        .then(data => {
            goodsViewDetails = data;
        })
        .catch(error => {
            console.error('Failed to get random goods : ', error);
        });

    return goodsViewDetails;
}

/**
 * (1) Need global variable (goodsViewList, goodsCategory, pageLoading, lastCallTime, delayDuration, page)
 *     let goodsViewList;           // 전체 상품 리스트
 *     let goodsCategory = "전체";
 *     let pageLoading = false;
 *     let lastCallTime = 0;       // 마지막 페이징 호출 시간
 *     let delayDuration = 2000;   // 페이징 딜레이 시간
 *     let page = 1;
 *
 * (2) Optional argument : query (goodsName)
 */
function addInfiniteScroll() {
    window.addEventListener('scroll', async () => {
        if (pageLoading) {
            return;
        }

        const targetScrollOffset = (document.body.offsetHeight * 90) / 100;     // 화면 하단에서 90% 이전에 로드 시작
        const isNearBottom = window.innerHeight + window.scrollY >= targetScrollOffset;

        if (isNearBottom && (Date.now() - lastCallTime) >= delayDuration) {
            pageLoading = true;
            let isPassed = false;
            let query = arguments[0];

            if (query != null) {
                isPassed = await displayGoodsListForNextPage(query);
            } else {
                isPassed = await displayGoodsListForNextPage();
            }

            if (isPassed) {
                await displayWishListGoods(document.getElementById('contentFrame'), getProductsByType(goodsViewList, goodsCategory));
                activeGoodsDetailsURL();
            } else {
                window.scrollTo(0, 0);
            }

            pageLoading = false;
            lastCallTime = Date.now();
        }
    });
}

/**
 * same need global variable like 'addInfiniteScroll()'
 */
async function displayGoodsListForNextPage() {
    try {
        let query = arguments[0];
        let response;

        if (query == null) {
            response = await fetch('/api/v1/goods?type=' + goodsCategory + '&page=' + page);
        } else {
            response = await fetch('/api/v1/goods?name=' + query + '&type=' + goodsCategory + '&page=' + page);
        }

        if (response.ok) {
            let nextGoodsViewList = await response.json();
            goodsViewList = goodsViewList.concat(nextGoodsViewList);
            updateItemListLength(goodsViewList.length);

            let isCreated = createItemListForNext(document.getElementsByClassName('wrap-category-product-list')[0], nextGoodsViewList);
            if (isCreated) {
                page++;
            }
            return true;
        } else {
            throw response;
        }
    } catch (error) {
        alert('페이지 가져오기 실패 : ' + error);
        return false;
    }
}