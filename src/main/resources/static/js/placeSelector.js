const rowSelect = document.getElementById('row');
const placeSelect = document.getElementById('place');

function updatePlaceOptions() {
    const selectedRow = rowSelect.value - 1;
    let availablePlaces = [];
    for (let i = 0; i < hall.seats[selectedRow].length; i++) {
        if (!hall.seats[selectedRow][i]) {
            availablePlaces.push(i);
        }
    }

    placeSelect.innerHTML = '';
    availablePlaces.forEach((num) => {
        const option = document.createElement('option');
        option.value = num + 1;
        option.textContent = num + 1;
        placeSelect.appendChild(option);
    });
}

rowSelect.addEventListener('change', updatePlaceOptions);
updatePlaceOptions();
