function toggleProfileBox() {
    var profileBox = document.getElementById("profileBox");
    if (profileBox.style.display === "block") {
      profileBox.style.display = "none";
    } else {
      profileBox.style.display = "block";
    }
  }
  
  // Đóng profile box khi nhấn vào chỗ khác trên trang
  document.addEventListener("click", function (event) {
    var profileBox = document.getElementById("profileBox");
    var avatar = document.querySelector(".avatar");
  
    if (event.target !== avatar && !avatar.contains(event.target) && event.target !== profileBox && !profileBox.contains(event.target)) {
      profileBox.style.display = "none";
    }
  });