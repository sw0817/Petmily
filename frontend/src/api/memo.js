import { createInstance } from "./index.js";

const instance = createInstance();
// const config = {
//   headers: { "access-token": localStorage.getItem("access-token") }
// };

async function writeMemo(memo, success, fail) {
  await instance
    .post(`memo`, JSON.stringify(memo))
    .then(success)
    .catch(fail);
}

function listMemo(articleno, success, fail) {
  instance
    .get(`memo/${articleno}`)
    .then(success)
    .catch(fail);
}

function modifyMemo(memo, success, fail) {
  instance
    .put(`memo`, JSON.stringify(memo))
    .then(success)
    .catch(fail);
}

function deleteMemo(obj, success, fail) {
  instance
    .delete(`memo/${obj.feed_id}/${obj.memo_id}`)
    .then(success)
    .catch(fail);
}

function getPetInfo(success, fail) {
  instance
    .get(`pet/selectAll`)
    .then(success)
    .catch(fail);
}

function getAllMember(success, fail) {
  instance
    .get(`member/getallmember`)
    .then(success)
    .catch(fail);
}

export { writeMemo, listMemo, modifyMemo, deleteMemo, getPetInfo, getAllMember };
