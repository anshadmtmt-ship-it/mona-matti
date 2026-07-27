/**
 * MONA MATTI — Architect Sketchbook Engine v14
 * =============================================================
 * Features:
 * 1. Sole Secret Entrance: Yellow Sticky Note Interaction
 *    - Animate sticky note lifting & paper darkening mask (300ms)
 *    - Opens modern floating 900px dark graphite MONA MATTI CMS
 * 2. Multi-Tab CMS Console (Hero Section, Product Details)
 * 3. Live Real-Time Site Update on SAVE CHANGES (No page reload)
 * 4. Physical HB Graphite Cursor Trail Engine (Document Coordinates & Wheel/Scroll Active)
 * 5. Static Document-Flow Handwritten Navigation Anchor Scroll
 * 6. Hero Product Pencil 3D Parallax Tilt & IntersectionObserver Scroll Reveal
 */

document.addEventListener('DOMContentLoaded', () => {

    /* ======================================================================
       1. PHYSICAL HB GRAPHITE CURSOR TRAIL ENGINE (DOCUMENT ACCURATE)
       ====================================================================== */
    const sheet  = document.getElementById('sketchbook-sheet');
    const canvas = document.getElementById('graphite-canvas');

    if (sheet && canvas) {
        const ctx = canvas.getContext('2d');
        let points = [];
        let animId = null;

        let lastClientX = -1;
        let lastClientY = -1;
        let isOverSheet = false;

        function resizeCanvas() {
            const dpr = Math.min(window.devicePixelRatio || 1, 2);
            canvas.width  = window.innerWidth  * dpr;
            canvas.height = window.innerHeight * dpr;
            ctx.scale(dpr, dpr);
        }

        resizeCanvas();
        window.addEventListener('resize', resizeCanvas);

        function addDocPoint(clientX, clientY) {
            const docX = clientX + window.scrollX;
            const docY = clientY + window.scrollY;
            const now  = performance.now();

            let speed = 1;
            if (points.length > 0) {
                const last = points[points.length - 1];
                const dist = Math.hypot(docX - last.docX, docY - last.docY);
                speed = Math.min(3, Math.max(0.5, dist / 8));
            }

            points.push({ docX, docY, time: now, speed });

            if (points.length > 130) {
                points.shift();
            }

            if (!animId) {
                animId = requestAnimationFrame(renderTrail);
            }
        }

        document.addEventListener('mousemove', (e) => {
            lastClientX = e.clientX;
            lastClientY = e.clientY;

            const sheetRect = sheet.getBoundingClientRect();
            isOverSheet = (
                e.clientX >= sheetRect.left &&
                e.clientX <= sheetRect.right &&
                e.clientY >= sheetRect.top &&
                e.clientY <= sheetRect.bottom
            );

            if (isOverSheet && !e.target.closest('a, button, input, textarea, select, .btn-solid, .btn-outline, .cms-input')) {
                addDocPoint(e.clientX, e.clientY);
            }
        });

        function handleScrollOrWheel() {
            if (lastClientX >= 0 && lastClientY >= 0) {
                const sheetRect = sheet.getBoundingClientRect();
                isOverSheet = (
                    lastClientX >= sheetRect.left &&
                    lastClientX <= sheetRect.right &&
                    lastClientY >= sheetRect.top &&
                    lastClientY <= sheetRect.bottom
                );

                if (isOverSheet) {
                    const elUnderPoint = document.elementFromPoint(lastClientX, lastClientY);
                    if (elUnderPoint && !elUnderPoint.closest('a, button, input, textarea, select, .btn-solid, .btn-outline, .cms-input')) {
                        addDocPoint(lastClientX, lastClientY);
                    }
                }
            }
        }

        window.addEventListener('scroll', handleScrollOrWheel, { passive: true });
        window.addEventListener('wheel',  handleScrollOrWheel, { passive: true });

        function renderTrail(currentTime) {
            ctx.clearRect(0, 0, window.innerWidth, window.innerHeight);

            const sheetRect = sheet.getBoundingClientRect();
            ctx.save();
            ctx.beginPath();
            ctx.rect(sheetRect.left, sheetRect.top, sheetRect.width, sheetRect.height);
            ctx.clip();

            const maxAge = 1100;
            points = points.filter(p => (currentTime - p.time) < maxAge);

            if (points.length >= 2) {
                const total = points.length;

                for (let i = 1; i < total; i++) {
                    const prev = points[i - 1];
                    const curr = points[i];

                    const prevScreenX = prev.docX - window.scrollX;
                    const prevScreenY = prev.docY - window.scrollY;
                    const currScreenX = curr.docX - window.scrollX;
                    const currScreenY = curr.docY - window.scrollY;

                    const progress = i / total;

                    let posAlpha = 1.0;
                    if (progress < 0.10) {
                        const tailFactor = progress / 0.10;
                        posAlpha = Math.pow(tailFactor, 1.8);
                    }

                    const age = currentTime - curr.time;
                    let timeAlpha = 1.0;
                    if (age > 200) {
                        timeAlpha = Math.max(0, 1 - ((age - 200) / 900));
                    }

                    const alpha = Math.min(1.0, posAlpha * timeAlpha * 0.95);
                    if (alpha <= 0.008) continue;

                    const midX = (prevScreenX + currScreenX) / 2;
                    const midY = (prevScreenY + currScreenY) / 2;
                    const baseWidth = Math.max(2.2, 2.8 - curr.speed * 0.2);

                    ctx.beginPath();
                    ctx.moveTo(prevScreenX, prevScreenY);
                    ctx.quadraticCurveTo(prevScreenX, prevScreenY, midX, midY);
                    ctx.strokeStyle = `rgba(52, 48, 44, ${alpha.toFixed(3)})`;
                    ctx.lineWidth   = baseWidth;
                    ctx.lineCap     = 'round';
                    ctx.lineJoin    = 'round';
                    ctx.stroke();

                    ctx.beginPath();
                    ctx.moveTo(prevScreenX + 0.5, prevScreenY - 0.4);
                    ctx.quadraticCurveTo(prevScreenX + 0.5, prevScreenY - 0.4, midX + 0.5, midY - 0.4);
                    ctx.strokeStyle = `rgba(38, 34, 31, ${(alpha * 0.42).toFixed(3)})`;
                    ctx.lineWidth   = Math.max(1, baseWidth * 0.45);
                    ctx.stroke();
                }

                ctx.restore();
                animId = requestAnimationFrame(renderTrail);
            } else {
                ctx.restore();
                animId = null;
            }
        }
    }

    /* ======================================================================
       2. SECRET FOOTER ENTRANCE & MONA MATTI CMS MODAL ENGINE
       ====================================================================== */
    const footerTrigger = document.getElementById('display-footer-copy');
    const paperDarken   = document.getElementById('sticky-paper-darken');
    const cmsBackdrop   = document.getElementById('cms-backdrop');
    const cmsCloseBtn   = document.getElementById('cms-close-btn');
    const cmsCancelBtn  = document.getElementById('cms-cancel-btn');

    function openCmsModal() {
        if (paperDarken) {
            paperDarken.classList.add('active');
        }

        // Smooth transition before popping CMS window
        setTimeout(() => {
            if (cmsBackdrop) {
                cmsBackdrop.style.display = 'flex';
                // Trigger reflow for CSS transition
                void cmsBackdrop.offsetWidth;
                cmsBackdrop.classList.add('open');
            }
        }, 150);
    }

    function closeCmsModal() {
        if (cmsBackdrop) {
            cmsBackdrop.classList.remove('open');
            setTimeout(() => {
                cmsBackdrop.style.display = 'none';
            }, 350);
        }
        if (paperDarken) {
            paperDarken.classList.remove('active');
        }
    }

    if (footerTrigger) {
        footerTrigger.addEventListener('click', openCmsModal);
    }

    if (cmsCloseBtn)  cmsCloseBtn.addEventListener('click', closeCmsModal);
    if (cmsCancelBtn) cmsCancelBtn.addEventListener('click', closeCmsModal);
    if (paperDarken)  paperDarken.addEventListener('click', closeCmsModal);

    // Sidebar Tab Switching Engine & Quick Edit Jumps
    const tabBtns  = document.querySelectorAll('.cms-tab-btn');
    const tabPanes = document.querySelectorAll('.cms-pane');

    function activateTab(tabId) {
        tabBtns.forEach(b => {
            if (b.getAttribute('data-tab') === tabId) {
                b.classList.add('active');
            } else {
                b.classList.remove('active');
            }
        });
        tabPanes.forEach(p => {
            if (p.id === tabId) {
                p.classList.add('active');
            } else {
                p.classList.remove('active');
            }
        });
    }

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const targetTab = btn.getAttribute('data-tab');
            activateTab(targetTab);
        });
    });

    document.querySelectorAll('.cms-quick-jump').forEach(jumpBtn => {
        jumpBtn.addEventListener('click', () => {
            const targetTab = jumpBtn.getAttribute('data-target');
            activateTab(targetTab);
        });
    });


    // Live Real-Time Admin Save Action (Instant Preview, No Reload)
    const cmsSaveBtn  = document.getElementById('cms-save-btn');
    const cmsResetBtn = document.getElementById('cms-reset-btn');

    if (cmsSaveBtn) {
        cmsSaveBtn.addEventListener('click', () => {
            const getVal = (id, fallback='') => {
                const el = document.getElementById(id);
                return el ? el.value : fallback;
            };

            const hTitle    = getVal('cms-hero-title', 'One Pencil. Infinite Possibilities.');
            const hSub      = getVal('cms-hero-subtitle', 'Replace the nib. Refresh the idea. Create without limits.');
            const btnText   = getVal('cms-btn-text', 'ORDER NOW');
            const btnLink   = getVal('cms-btn-link', '#product');
            const pName     = getVal('cms-product-name', 'MONA MATTI Pencil');
            const price     = getVal('cms-price', '49.99');
            const stock     = getVal('cms-stock-status', 'IN STOCK');
            const pShortDesc= getVal('cms-product-short-desc', 'One tool. Many ideas. Zero limits.');
            const material  = getVal('cms-spec-material', 'Anodized Aluminium & Brass');
            const lengthSpec= getVal('cms-spec-length', '140mm Barrel');
            const weightSpec= getVal('cms-spec-weight', '28g');
            const nibSize   = getVal('cms-spec-nib-size', '0.7mm HB Lead');
            const webTitle  = getVal('cms-website-title', 'MONA MATTI');

            if (webTitle) document.title = webTitle;

            const elBrand = document.getElementById('display-brand-name');
            if (elBrand) elBrand.textContent = 'MONA MATTI';

            const elSvgBrand = document.getElementById('pencil-svg-brand');
            if (elSvgBrand) elSvgBrand.textContent = 'MONA MATTI';

            document.querySelectorAll('.footer-brand').forEach(f => f.textContent = 'MONA MATTI');

            const elHTitle = document.getElementById('display-hero-title');
            if (elHTitle) elHTitle.innerHTML = hTitle.replace(/\n/g, '<br>');

            const elHSub = document.getElementById('display-hero-subtitle');
            if (elHSub) elHSub.innerHTML = hSub.replace(/\n/g, '<br>');

            const elBtnText = document.getElementById('display-btn-text');
            if (elBtnText) elBtnText.textContent = btnText;

            const elShopBtn = document.getElementById('btn-shop');
            if (elShopBtn && btnLink) elShopBtn.setAttribute('href', btnLink);

            const elPrice = document.getElementById('display-price');
            if (elPrice) elPrice.textContent = price;

            const elStock = document.getElementById('display-stock-status');
            if (elStock) elStock.textContent = stock;

            const elPName = document.getElementById('display-product-name');
            if (elPName) elPName.textContent = pName;

            const elPDesc = document.getElementById('display-product-desc');
            if (elPDesc) elPDesc.innerHTML = pShortDesc.replace(/\n/g, '<br>');

            const elMat = document.getElementById('display-spec-material');
            if (elMat) elMat.textContent = material;

            const elNib = document.getElementById('display-spec-nib');
            if (elNib) elNib.textContent = nibSize;

            const elW = document.getElementById('display-spec-weight');
            if (elW) elW.textContent = `${weightSpec} · ${lengthSpec}`;

            const elCopy = document.getElementById('display-footer-copy');
            if (elCopy) elCopy.textContent = '© 2026 MONA MATTI. All rights reserved.';

            // Update Dashboard Overview Cards
            const dashName  = document.getElementById('dash-prod-name');
            if (dashName) dashName.textContent = pName;

            const dashPrice = document.getElementById('dash-price');
            if (dashPrice) dashPrice.textContent = `$${price}`;

            const dashStock = document.getElementById('dash-stock');
            if (dashStock) dashStock.textContent = stock;

            const dashUpdated = document.getElementById('dash-updated');
            if (dashUpdated) {
                const now = new Date();
                dashUpdated.textContent = now.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            }

            closeCmsModal();
        });
    }

    if (cmsResetBtn) {
        cmsResetBtn.addEventListener('click', () => {
            const setVal = (id, val) => {
                const el = document.getElementById(id);
                if (el) el.value = val;
            };
            setVal('cms-hero-title', 'One Pencil. Infinite Possibilities.');
            setVal('cms-hero-subtitle', 'Replace the nib. Refresh the idea. Create without limits.');
            setVal('cms-btn-text', 'ORDER NOW');
            setVal('cms-btn-link', '#product');
            setVal('cms-product-name', 'MONA MATTI Pencil');
            setVal('cms-price', '49.99');
            setVal('cms-stock-status', 'IN STOCK');
            setVal('cms-buy-btn-text', 'ORDER NOW');
            setVal('cms-product-short-desc', 'One tool. Many ideas. Zero limits.');
            setVal('cms-product-long-desc', 'Anodized matte aluminum body with 5 interlocking 0.7mm HB artist-grade graphite nibs and a heavy-duty brass push mechanism.');
            setVal('cms-spec-material', 'Anodized Aluminium & Brass');
            setVal('cms-spec-length', '140mm Barrel');
            setVal('cms-spec-weight', '28g');
            setVal('cms-spec-colour', 'Matte Black & Brushed Brass');
            setVal('cms-spec-nib-size', '0.7mm HB Lead');
            setVal('cms-spec-nibs-count', '5 Lead Nibs');
            setVal('cms-spec-replaceable', 'Yes');
        });
    }

    /* ======================================================================
       3. HANDWRITTEN PAPER NAVIGATION — Smooth Scroll
       ====================================================================== */
    const navLinks = document.querySelectorAll('.paper-nav-link');

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            const target = document.querySelector(link.getAttribute('href'));
            if (target) {
                e.preventDefault();
                target.scrollIntoView({ behavior: 'smooth', block: 'start' });
            }
        });
    });

    /* ======================================================================
       4. HERO PRODUCT PENCIL — 3D Parallax Tilt on Mouse Move
       ====================================================================== */
    const heroSection = document.getElementById('home');
    const heroPencil  = document.getElementById('hero-pencil');

    if (heroSection && heroPencil) {
        heroSection.addEventListener('mousemove', (e) => {
            const rect = heroSection.getBoundingClientRect();
            const dx   = (e.clientX - rect.left  - rect.width  / 2) / (rect.width  / 2);
            const dy   = (e.clientY - rect.top   - rect.height / 2) / (rect.height / 2);
            heroPencil.style.transform =
                `perspective(900px) rotateX(${dy * -7}deg) rotateY(${dx * 7}deg) scale(1.02)`;
        });

        heroSection.addEventListener('mouseleave', () => {
            heroPencil.style.transform =
                'perspective(900px) rotateX(0deg) rotateY(0deg) scale(1)';
        });
    }

    /* ======================================================================
       5. SCROLL REVEAL — Fade + slide up for every .reveal element
       ====================================================================== */
    const revealEls = document.querySelectorAll('.reveal');

    const revealObs = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                revealObs.unobserve(entry.target);
            }
        });
    }, { threshold: 0.10, rootMargin: '0px 0px -30px 0px' });

    revealEls.forEach(el => revealObs.observe(el));

    /* ======================================================================
       6. MECHANISM DEMO MODAL
       ====================================================================== */
    const demoBtn     = document.getElementById('btn-demo');
    const demoModalEl = document.getElementById('demoModal');
    let demoModal = null;

    if (demoModalEl && typeof bootstrap !== 'undefined') {
        demoModal = new bootstrap.Modal(demoModalEl);
    }

    if (demoBtn && demoModal) {
        demoBtn.addEventListener('click', () => demoModal.show());
    }

});
