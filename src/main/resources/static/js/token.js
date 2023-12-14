const token = searchParam('token');

if (token) {
  // localStorage에 저장
  // localStorage.setItem("access_token", token);
  
  // 쿠키에 저장
  // 일단 쿠키에만 저장하기로 함 (서버에서 확인할 때도 쿠키에서 확인하도록 함)
  const expirationDate = new Date();
  expirationDate.setDate(expirationDate.getDate() + 1);
  document.cookie = `access_token=${token}; path=/; expires=${expirationDate.toUTCString()};`

  // URL에서 토큰 제거
  removeTokenFromURL();
}

function searchParam(key) {
  return new URLSearchParams(location.search).get(key);
}

// 쿠키 등록 후 URL에서 token 쿼리스트링 제거
function removeTokenFromURL() {
  const urlWithoutToken = window.location.href.replace(/[?&]token=[^&#]*/g, '');
  history.replaceState({}, document.title, urlWithoutToken);
}