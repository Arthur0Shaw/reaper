import { useRouter } from 'next/router';
import { useEffect } from 'react';

const withAuth = (WrappedComponent) => {
  return (props) => {
    const router = useRouter();

    useEffect(() => {
      const token = localStorage.getItem('token');
      if (!token) {
        router.push('authentication/login');  // Redirect to login if not authenticated
      }
    }, [router]);

    return <WrappedComponent {...props} />;
  };
};

export default withAuth;
