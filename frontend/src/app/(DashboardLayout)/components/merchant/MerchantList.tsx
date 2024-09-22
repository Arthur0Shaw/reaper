"use client";

import React, { useEffect, useState } from "react";
import axios from "axios";
import { List, ListItem, ListItemText, CircularProgress, Typography, Box, Paper } from "@mui/material";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";

const MerchantList = ({ onSelectMerchant }) => {
  const [merchants, setMerchants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchMerchants = async () => {
    try {
      const token = localStorage.getItem("token");
      const response = await axios.get("http://localhost:8080/api/v1/users/merchantList", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      setMerchants(response.data);
      setLoading(false);
    } catch (error) {
      setError("Failed to load merchants.");
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMerchants();
  }, []);

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="150px">
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="150px">
        <ErrorOutlineIcon color="error" sx={{ fontSize: 50, mr: 2 }} />
        <Typography color="error">{error}</Typography>
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h6" gutterBottom sx={{ fontWeight: "600", color: "#333" }}>
        Select a Merchant
      </Typography>
      <List sx={{ maxHeight: "400px", overflow: "auto", backgroundColor: "#fdfdfd", borderRadius: "12px" }}>
        {merchants.map((merchant) => (
          <ListItem
            button
            key={merchant.id}
            onClick={() => onSelectMerchant(merchant)}
            sx={{
              backgroundColor: "#f9f9f9",
              mb: 1,
              borderRadius: "8px",
              transition: "background-color 0.3s, box-shadow 0.3s",
              '&:hover': { backgroundColor: "#e0f7fa", boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)" },
            }}
          >
            <ListItemText primary={merchant.businessName} sx={{ color: "#333" }} />
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export default MerchantList;
