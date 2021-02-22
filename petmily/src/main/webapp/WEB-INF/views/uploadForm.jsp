<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta charset="EUC-KR" />
    <title>Insert title here</title>
  </head>
  <body>
    <fieldset>
      <legend>파일 업로드</legend>
      <form
        action="http://localhost:8000/pet/insertProc"
        method="POST"
        enctype="multipart/form-data"
      >
        <table>
          <tr>
            <td>사용자 아이디</td>
            <td><input type="text" name="user_id" /></td>
          </tr>
          <tr>
            <td>펫 이름</td>
            <td><input type="text" name="pet_name" /></td>
          </tr>
          <tr>
            <td>펫 종류</td>
            <td><input type="text" name="pet_kind" /></td>
          </tr>
          <tr>
            <td>성별</td>
            <td><input type="text" name="pet_gender" /></td>
          </tr>
          <tr>
            <td>나이</td>
            <td><input type="text" name="pet_age" /></td>
          </tr>
          <tr>
            <td>내용</td>
            <td><input type="text" name="pet_content" /></td>
          </tr>
          <tr>
            <td>사진 등록일</td>
            <td>
             <input type="text" name="create_date" value="20101212">
            </td>
          </tr>
          <tr>
            <td>
              <label for="fileupload"></label>
              <input
                type="file"
                class="form-control"
                name="fileupload"
                id="fileupload"
              />
            </td>
          </tr>
          <tr>
            <td>
              <input type="submit" name="submit" value="완료" />
              <input type="reset" name="reset" value="리셋" />
            </td>
          </tr>
        </table>
      </form>
    </fieldset>
  </body>
</html>



