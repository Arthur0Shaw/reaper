"use client";

import React, { useState, useEffect } from "react";
import {
  Box,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  CircularProgress,
  Paper,
} from "@mui/material";
import { IconMoodSad } from "@tabler/icons-react";
import axios from "axios";
import TableContainer from "@mui/material/TableContainer";

const LoginHistoryTable = () => {
  const [loginHistory, setLoginHistory] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Fetch the login history
  const fetchLoginHistory = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem("token"); // Assuming token is stored in localStorage
      const response = await axios.get("http://localhost:8080/api/v1/users/loginHistory", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setLoginHistory(response.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching login history:", error);
      setError("Failed to load login history.");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLoginHistory(); // Fetch the login history when the component mounts
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="200px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Typography color="error" textAlign="center">
        {error}
      </Typography>
    );
  }

  if (!loginHistory.length) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" flexDirection="column" height="300px" sx={{ backgroundColor: "#e3f2fd", borderRadius: "12px", padding: "2rem", textAlign: "center" }}>
        <IconMoodSad size={48} color="#90a4ae" />
        <Typography variant="h6" color="textSecondary" sx={{ mt: 2 }}>
          No login history found.
        </Typography>
      </Box>
    );
  }

  return (
    <Box sx={{ backgroundColor: "#fff", borderRadius: "16px", boxShadow: "0 4px 10px rgba(0, 0, 0, 0.05)", padding: "2rem", mt: 4 }}>
      {/* Title Section */}
      <Typography variant="h5" sx={{ mb: 3, textAlign: "center", fontWeight: "bold", color: "#1565c0", letterSpacing: "0.5px" }}>
        Login History
      </Typography>

      {/* Table Section */}
      <TableContainer component={Paper} sx={{ borderRadius: "12px" }}>
        <Table sx={{ whiteSpace: "nowrap" }}>
          <TableHead sx={{ backgroundColor: "#f5f5f5", borderBottom: "2px solid #e0e0e0" }}>
            <TableRow>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  User Name
                </Typography>
              </TableCell>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  User Type
                </Typography>
              </TableCell>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Description
                </Typography>
              </TableCell>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  IP Address
                </Typography>
              </TableCell>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  OS
                </Typography>
              </TableCell>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Browser
                </Typography>
              </TableCell>
              <TableCell sx={{ backgroundColor: "#e3f2fd" }}>
                <Typography variant="subtitle2" fontWeight={600}>
                  Date & Time
                </Typography>
              </TableCell>
            </TableRow>
          </TableHead>

          <TableBody>
            {loginHistory.map((loginEvent) => (
              <TableRow key={loginEvent.id} sx={{ "&:hover": { backgroundColor: "#f9f9f9" } }}>
                <TableCell>
                  <Typography
                    sx={{
                      fontSize: "15px",
                      fontWeight: "500",
                      color: "#424242",
                    }}
                  >
                    {loginEvent.userName}
                  </Typography>
                </TableCell>
                <TableCell>{loginEvent.userType}</TableCell>
                <TableCell>{loginEvent.description}</TableCell>
                <TableCell>{loginEvent.ipAddress}</TableCell>
                <TableCell>{loginEvent.os}</TableCell>
                <TableCell>{loginEvent.browser}</TableCell>
                <TableCell>{new Date(loginEvent.createdAt).toLocaleString()}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default LoginHistoryTable;
