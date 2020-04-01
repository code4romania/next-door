export const isExpired = token => {
  const payload = getPayload(token);
  return !payload || payload.exp * 1000 - Date.now() < 0;
};

export const getPayload = token => {
  const [, key] = token ? token.split(".") : [];
  return key && JSON.parse(atob(key));
};
