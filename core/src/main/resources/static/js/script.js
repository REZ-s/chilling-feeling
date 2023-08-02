/***
 * Bottom Menu Container
 * @param path
 */
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

/***
 * Item List Container Using Category, Search
 * @param parentElement
 */
function createItemList(parentElement, products) {
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

    wrapCategoryContainer.appendChild(wrapCategoryProductList);

    parentElement.appendChild(wrapCategoryContainer);

    const wrapCountingRangeEmpty = document.createElement("div");
    wrapCountingRangeEmpty.className = "wrap-counting-range";
    wrapCountingRangeEmpty.id = "surplusEmptySpace";

    parentElement.appendChild(wrapCountingRangeEmpty);
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
function createEmptyItemList(parentElement) {
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
 * arguments = { parentElement, goodsViewList, type }
 */
function displayItemList() {
    if (arguments.length < 2) {
        return;
    }

    const parentElement = arguments[0];
    const goodsViewList = arguments[1];

    if (arguments.length === 2) {
        createBaseItemList(parentElement, getProducts(goodsViewList));
    } else if (arguments.length === 3) {
        const type = arguments[2];

        if (type === "전체" || type === "All" || type === "all" || type === "ALL") {
            createBaseItemList(parentElement, getProducts(goodsViewList));
        } else {
            createBaseItemList(parentElement, getProductsByType(goodsViewList, type));
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

function createBaseItemList(parentElement, products) {
    if (products == null || products.length === 0) {
        createEmptyItemList(parentElement);
    } else {
        createItemList(parentElement, products);
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