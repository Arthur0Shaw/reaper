"use client";

import React, { useState } from "react";
import {
  Box,
  Grid,
  TextField,
  Typography,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  Button,
  InputAdornment,
  Paper,
} from "@mui/material";
import { AccountBalance, TrendingUp, AttachMoney } from "@mui/icons-material"; // Icons
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";  // Import ToastContainer and toast
import "react-toastify/dist/ReactToastify.css";  // Import Toast styles

const RoutingPage = () => {
  const [selectedBank, setSelectedBank] = useState(""); // Bank dropdown state
  const [transactionCount, setTransactionCount] = useState("");
  const [thresholdAmount, setThresholdAmount] = useState("");
  const [maxTransactionAmount, setMaxTransactionAmount] = useState("");
  const [errors, setErrors] = useState({
    bank: false,
    transactionCount: false,
    thresholdAmount: false,
    maxTransactionAmount: false,
  });

  // Bank options
  const bankOptions = ["Yes Bank", "SBI", "HDFC", "ICICI", "BOB", "Bandhan Bank", "IDFC", "Axis Bank"];

  // Handle dropdown change
  const handleBankChange = (event) => {
    setSelectedBank(event.target.value);
    if (event.target.value) setErrors((prev) => ({ ...prev, bank: false }));
  };

  // Handle save action
  const handleSave = async () => {
    if (!selectedBank || !transactionCount || !thresholdAmount || !maxTransactionAmount) {
      setErrors({
        bank: !selectedBank,
        transactionCount: !transactionCount,
        thresholdAmount: !thresholdAmount,
        maxTransactionAmount: !maxTransactionAmount,
      });
      toast.error("Please fill out all fields");
      return;
    }

    const payload = {
      bank: selectedBank,
      totalTxnCount: transactionCount,
      thresholdTxnAmount: thresholdAmount,
      maximumTxnAmount: maxTransactionAmount,
    };

    try {
      const token = localStorage.getItem("token"); // Assuming token is stored in localStorage
      const response = await axios.post("http://localhost:8080/api/v1/acquirer/txnRoutingDetails", payload, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.status === 200 || response.status === 201) {
        toast.success("Routing details saved successfully!");
      } else {
        toast.error("Failed to save routing details. Please try again.");
      }
    } catch (error) {
      toast.error("Error occurred. Please try again.");
      console.error("API error:", error);
    }
  };

  return (
    <Box>
      <ToastContainer />  {/* ToastContainer added here */}
      <Paper
        elevation={3}
        sx={{
          padding: 3,
          backgroundColor: "#ffffff",
          borderRadius: "16px",
          boxShadow: "0 4px 10px rgba(0, 0, 0, 0.05)",
          minWidth: "100vh",
        }}
      >
        <Grid container spacing={4}>
          {/* Left Side - Bank Dropdown and Inputs */}
          <Grid item xs={12} md={6}>
            <Box p={19}>
              <Typography variant="h5" gutterBottom sx={{ fontWeight: "bold", color: "#1f4cb6" }}>
                Bank and Routing Setup
              </Typography>

              {/* Bank Dropdown */}
              <FormControl fullWidth variant="outlined" sx={{ marginBottom: 3 }} error={errors.bank}>
                <InputLabel>Select Bank</InputLabel>
                <Select value={selectedBank} onChange={handleBankChange} label="Select Bank">
                  {bankOptions.map((bank) => (
                    <MenuItem key={bank} value={bank}>
                      <AccountBalance sx={{ mr: 1 }} /> {bank}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              {/* Total Transaction Count */}
              <TextField
                fullWidth
                label="Total Transaction Count"
                variant="outlined"
                value={transactionCount}
                onChange={(e) => {
                  setTransactionCount(e.target.value);
                  if (e.target.value) setErrors((prev) => ({ ...prev, transactionCount: false }));
                }}
                error={errors.transactionCount}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <TrendingUp />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              {/* Threshold Transaction Amount */}
              <TextField
                fullWidth
                label="Threshold Transaction Amount"
                variant="outlined"
                value={thresholdAmount}
                onChange={(e) => {
                  setThresholdAmount(e.target.value);
                  if (e.target.value) setErrors((prev) => ({ ...prev, thresholdAmount: false }));
                }}
                error={errors.thresholdAmount}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <AttachMoney />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              {/* Maximum Transaction Amount */}
              <TextField
                fullWidth
                label="Maximum Transaction Amount"
                variant="outlined"
                value={maxTransactionAmount}
                onChange={(e) => {
                  setMaxTransactionAmount(e.target.value);
                  if (e.target.value) setErrors((prev) => ({ ...prev, maxTransactionAmount: false }));
                }}
                error={errors.maxTransactionAmount}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <AttachMoney />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />
            </Box>
          </Grid>

          {/* Right Side - Save Button */}
          <Grid item xs={12} md={6}>
            <Box
              display="flex"
              justifyContent="center"
              alignItems="center"
              flexDirection="column"
              height="100%"
              p={3}
            >
              <Typography variant="h5" gutterBottom sx={{ fontWeight: "bold", color: "#1f4cb6", marginBottom: 2 }}>
                Save Your Routing Details
              </Typography>

              {/* Save Button */}
              <Button
                variant="contained"
                onClick={handleSave}
                sx={{
                  px: 5,
                  py: 1.2,
                  borderRadius: '12px',
                  fontSize: '14px',
                  backgroundColor: '#1976d2',
                  '&:hover': {
                    backgroundColor: '#1565c0',
                  },
                  boxShadow: 'none', // Minimalistic button style
                }}
              >
                Save
              </Button>
            </Box>
          </Grid>
        </Grid>
      </Paper>
    </Box>
  );
};

export default RoutingPage;
