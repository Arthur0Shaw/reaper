'use client';

import { useState } from 'react';
import { Box, Grid, Stack, Button, Typography, TextField, FormControl } from '@mui/material';
import PageContainer from '@/app/(DashboardLayout)/components/container/PageContainer';
import DashboardCard from '@/app/(DashboardLayout)/components/shared/DashboardCard';
import axios from 'axios';

const RegisterPage = () => {
  const [formData, setFormData] = useState({
    business_name: "",
    password: "",
    mobile: "",
    contact_person_name: "",
    email: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.id]: e.target.value,
    });
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setMessage("");

    try {
      await axios.post(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/users/register`, formData);
      setMessage("Registration successful!");
    } catch (err) {
      setError("Registration failed. Please check the details.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <PageContainer title="Register" description="User Registration Page">
      {/* Box wrapper to apply styling */}
      <Box sx={{ maxWidth: '800px', margin: '0 auto', padding: '2rem' }}>
        <DashboardCard title="Register Your Business">
          <form onSubmit={handleSubmit}>
            <Stack spacing={3} alignItems="center">
              <Grid container spacing={3} justifyContent="center">
                {/* Column 1 */}
                <Grid item xs={12} sm={6}>
                  <FormControl fullWidth>
                    <TextField
                      label="Business Name"
                      id="business_name"
                      value={formData.business_name}
                      onChange={handleChange}
                      variant="outlined"
                      fullWidth
                      required
                      sx={{
                        height: '48px',
                        borderRadius: '8px',
                        boxShadow: 'none',
                        backgroundColor: '#f9f9f9',
                      }}
                    />
                  </FormControl>
                </Grid>

                {/* Column 2 */}
                <Grid item xs={12} sm={6}>
                  <FormControl fullWidth>
                    <TextField
                      label="Contact Person Name"
                      id="contact_person_name"
                      value={formData.contact_person_name}
                      onChange={handleChange}
                      variant="outlined"
                      fullWidth
                      required
                      sx={{
                        height: '48px',
                        borderRadius: '8px',
                        boxShadow: 'none',
                        backgroundColor: '#f9f9f9',
                      }}
                    />
                  </FormControl>
                </Grid>

                {/* Column 1 */}
                <Grid item xs={12} sm={6}>
                  <FormControl fullWidth>
                    <TextField
                      label="Email"
                      id="email"
                      type="email"
                      value={formData.email}
                      onChange={handleChange}
                      variant="outlined"
                      fullWidth
                      required
                      sx={{
                        height: '48px',
                        borderRadius: '8px',
                        boxShadow: 'none',
                        backgroundColor: '#f9f9f9',
                      }}
                    />
                  </FormControl>
                </Grid>

                {/* Column 2 */}
                <Grid item xs={12} sm={6}>
                  <FormControl fullWidth>
                    <TextField
                      label="Mobile"
                      id="mobile"
                      value={formData.mobile}
                      onChange={handleChange}
                      variant="outlined"
                      fullWidth
                      required
                      sx={{
                        height: '48px',
                        borderRadius: '8px',
                        boxShadow: 'none',
                        backgroundColor: '#f9f9f9',
                      }}
                    />
                  </FormControl>
                </Grid>

                {/* Full-Width Password Input */}
                <Grid item xs={12} sm={6}>
                  <FormControl fullWidth>
                    <TextField
                      label="Password"
                      id="password"
                      type="password"
                      value={formData.password}
                      onChange={handleChange}
                      variant="outlined"
                      fullWidth
                      required
                      sx={{
                        height: '48px',
                        borderRadius: '8px',
                        boxShadow: 'none',
                        backgroundColor: '#f9f9f9',
                      }}
                    />
                  </FormControl>
                </Grid>
              </Grid>

              {/* Display error or success messages */}
              {error && (
                <Typography color="error" sx={{ fontWeight: '500' }}>
                  {error}
                </Typography>
              )}
              {message && (
                <Typography color="primary" sx={{ fontWeight: '500' }}>
                  {message}
                </Typography>
              )}

              {/* Submit Button */}
              <Button
                type="submit"
                variant="contained"
                color="primary"
                disabled={loading}
                sx={{
                  height: '48px',
                  width: '180px',
                  borderRadius: '6px',
                  fontWeight: '500',
                  boxShadow: 'none',
                  backgroundColor: '#1976d2',
                  '&:hover': {
                    backgroundColor: '#1565c0',
                  },
                }}
              >
                {loading ? 'Registering...' : 'Register'}
              </Button>
            </Stack>
          </form>
        </DashboardCard>
      </Box>
    </PageContainer>
  );
};

export default RegisterPage;
