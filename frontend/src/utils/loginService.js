import axios from 'axios';

const loginService = async (email, password) => {
  try {
    const response = await axios.post('http://localhost:8080/api/v1/users/login', {
      email,
      password,
    });
    return response.data; // Return the token and message
  } catch (error) {
    console.error('Login API Error:', error.response || error.message); // Log detailed error
    throw new Error(error.response?.data?.message || 'Login failed');
  }
};

export default loginService;
