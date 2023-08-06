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

    bottomHomeButtons.forEach(function(button) {
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
    countingProductText.innerText = "총 " + products.length + "개";
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
    buttonHeart.className = 'ic_heart_24px btn-heart';

    const imageHeart = document.createElement('img');
    imageHeart.src = '/images/ic_heart_24px.png';

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
 * arguments = { parentElement, goodsViewList, type : optional }
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

    connectItemDetails();
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

    connectItemDetails();
}

function getProducts(goodsViewList) {
    return goodsViewList;
}

function getProductsByType(goodsViewList, type) {
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

function connectItemDetails() {
    let goodsContainers = document.getElementsByClassName('product-list01-photo-container');

    for (let goodsContainer of goodsContainers) {
        goodsContainer.addEventListener('click', function () {
            location.href = '/goods/' + goodsContainer.id.toString();
        });
    }
}

function applyPlusBtnElements() {
    plusBtnElements = document.querySelectorAll('[class*="ic_plus"]');
    plusBtnElements.forEach(function(plusBtn) {
        plusBtn.addEventListener('click', function() {
            let parentElement = plusBtn.parentElement;
            let goodsCountElement = parentElement.querySelector('[id*="goodsCount"]');
            let goodsCount = parseInt(goodsCountElement.innerText);

            goodsCountElement.innerText = (goodsCount + 1).toString();
        });
    });
}

function applyMinusBtnElements() {
    minusBtnElements = document.querySelectorAll('[class*="ic_minus"]');
    minusBtnElements.forEach(function(minusBtn) {
        minusBtn.addEventListener('click', function() {
            let parentElement = minusBtn.parentElement;
            let goodsCountElement = parentElement.querySelector('[id*="goodsCount"]');
            let goodsCount = parseInt(goodsCountElement.innerText);

            if (goodsCount > 1) {
                goodsCountElement.innerText = (goodsCount - 1).toString();
            }
        });
    });
}

/***
 * 상품을 위시리스트(좋아요)에 넣는다.
 */
function addWishList() {

}

/***
 * 상품을 장바구니에 넣는다.
 */
function addCartList() {

}

/**
 * 주문하기
 */
function orderGoodsList() {
    // 결제 과정은 생략되어 있으므로, 재고가 있으면 주문이 완료 되었다로 간략화
    // 0원 이라고 생각하자.
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
        productsFrame.style.cursor = "grabbing";
    });

    productsFrame.addEventListener("mouseenter", () => {
        productsFrame.style.cursor = "grab";
    });

    productsFrame.addEventListener("mouseup", () => {
        productsFrame.style.cursor = "grab";
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
