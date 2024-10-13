"use client"; // Ensure this is the first line

import React, { useState } from "react";
import { Box, Typography, Button, Stack } from "@mui/material";
import { useRouter } from "next/navigation"; // Client-side routing
import { toast } from "react-toastify";
import CustomTextField from "@/app/(DashboardLayout)/components/forms/theme-elements/CustomTextField";
import loginService from "@/utils/loginService";

const AuthLogin = ({ title, subtitle, subtext }: any) => {
  const [email, setEmail] = useState(""); // State for email
  const [password, setPassword] = useState(""); // State for password
  const [error, setError] = useState("");
  const router = useRouter(); // Safely using useRouter after adding "use client"

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError("");

    try {
      const { token, message } = await loginService(email, password); // API call
      localStorage.setItem("token", token); // Store the token in localStorage
      toast.success(message); // Display success toast
      router.push("/dashboard"); // Redirect to the dashboard
    } catch (err: any) {
      setError(err.message); // Set error message
      toast.error(err.message); // Display error toast
    }
  };

  return (
    <>
      {title ? (
        <Typography fontWeight="700" variant="h2" mb={1}>
          {title}
        </Typography>
      ) : null}

      {subtext}

      <form onSubmit={handleLogin}>
        <Stack>
          <Box>
            <Typography
              variant="subtitle1"
              fontWeight={600}
              component="label"
              htmlFor="email"
              mb="5px"
            >
              Email
            </Typography>
            <CustomTextField
              variant="outlined"
              fullWidth
              value={email}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setEmail(e.target.value)} // Capture email input
              required
            />
          </Box>
          <Box mt="25px">
            <Typography
              variant="subtitle1"
              fontWeight={600}
              component="label"
              htmlFor="password"
              mb="5px"
            >
              Password
            </Typography>
            <CustomTextField
              type="password"
              variant="outlined"
              fullWidth
              value={password}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setPassword(e.target.value)} // Capture password input
              required
            />
          </Box>
        </Stack>

        {error && (
          <Typography color="error" variant="body2" mt={2}>
            {error}
          </Typography>
        )}

        <Box mt={2}>
          <Button
            color="primary"
            variant="contained"
            size="large"
            fullWidth
            type="submit"
          >
            Sign In
          </Button>
        </Box>
      </form>

      {subtitle}
    </>
  );
};

export default AuthLogin;
