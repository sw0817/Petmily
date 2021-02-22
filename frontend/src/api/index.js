import axios from "axios";

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

function createInstance() {
  const instance = axios.create({
    baseURL: SERVER_URL,
    headers: {
      "Content-Type": "application/json"
    }
  });
  return instance;
}

export { createInstance };
