import { useRouter } from 'next/router';
import { useEffect } from 'react';

const Logout = () => {
  const router = useRouter();

  useEffect(() => {
    localStorage.removeItem('token');  // Clear token
    router.push('authentication/login');  // Redirect to login
  }, [router]);

  return null;
};

export default Logout;
