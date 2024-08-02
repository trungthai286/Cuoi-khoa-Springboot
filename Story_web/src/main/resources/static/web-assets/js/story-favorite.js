const btnFavorite = document.getElementById('btn-favorite');

btnFavorite.addEventListener('click', function () {
    if (isFavorite) {
        // Xóa khỏi danh sách yêu thích
        removeFromFavorite(story.id);
    } else {
        // Thêm vào danh sách yêu thích
        addToFavorite(story.id);
    }
})

const addToFavorite = (storyId) => {````
    axios.post(`/api/favorites?storyId=${storyId}`)
        .then(response => {
            toastr.success('Đã thêm vào danh sách yêu thích')
            isFavorite = true;
            btnFavorite.innerText = "Xóa khỏi yêu thích"
        })
        .catch(error => {
            console.log(error)
            toastr.error('Đã có lỗi xảy ra')
        })
}

const removeFromFavorite = (storyId) => {
    axios.delete(`/api/favorites?storyId=${storyId}`)
        .then(response => {
            toastr.success('Đã xóa khỏi danh sách yêu thích')
            isFavorite = false;
            btnFavorite.innerText = "Thêm vào yêu thích"
        })
        .catch(error => {
            console.log(error)
            toastr.error('Đã có lỗi xảy ra')
        })
}