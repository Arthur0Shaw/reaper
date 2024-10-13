import axios from 'axios';

const fetchDashboardData = async () => {
  try {
    const token = localStorage.getItem('token'); // Retrieve the token from localStorage
    const response = await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/transaction/dashboard`, 
      {
        // Payload for the POST request
        dateIndexFrom: "20240916",
        dateIndexTo: "20240917",
        status: null,
      },
      {
        headers: {
          Authorization: `Bearer ${token}`, // Include the token in the Authorization header
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error('API Error:', error.response || error.message);
    throw new Error('Failed to fetch dashboard data');
  }
};

export default fetchDashboardData;
