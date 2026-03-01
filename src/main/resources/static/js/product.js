// 数量変更時に価格を更新する
const quantitySelect = document.getElementById('quantity');
const basePrice = parseInt(document.getElementById('base-price')?.dataset.price || 0);
const baseTaxPrice = parseInt(document.getElementById('base-price')?.dataset.taxPrice || 0);

if (quantitySelect) {
    quantitySelect.addEventListener('change', function() {
        const qty = parseInt(this.value);
        document.getElementById('display-price').textContent = '¥' + (basePrice * qty).toLocaleString();
        document.getElementById('display-tax-price').textContent = '¥' + (baseTaxPrice * qty).toLocaleString();
    });
}