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
  IconButton,
} from "@mui/material";
import { AccountBalance, TrendingUp, AttachMoney } from "@mui/icons-material"; // Icons
import { toast } from "react-toastify";

const RoutingPage = () => {
  const [selectedBank, setSelectedBank] = useState(""); // Bank dropdown state
  const [transactionCount, setTransactionCount] = useState("");
  const [thresholdAmount, setThresholdAmount] = useState("");
  const [maxTransactionAmount, setMaxTransactionAmount] = useState("");

  // Bank options
  const bankOptions = ["Yes Bank", "SBI", "HDFC", "ICICI", "BOB", "Bandhan Bank", "IDFC", "Axis Bank"];

  // Handle dropdown change
  const handleBankChange = (event) => {
    setSelectedBank(event.target.value);
  };

  // Handle save action
  const handleSave = () => {
    if (!selectedBank || !transactionCount || !thresholdAmount || !maxTransactionAmount) {
      toast.error('Please fill out all fields');
    } else {
      toast.success('Routing details saved successfully!');
      // Save logic here
    }
  };

  return (
    <Box>
      <Paper
        elevation={3}
        sx={{
          padding: 3,
          backgroundColor: "#ffffff",
          borderRadius: "16px",
          boxShadow: "0 4px 10px rgba(0, 0, 0, 0.05)",
          minWidth: '100vh',
          // margin: 'auto', // Centering the card
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
              <FormControl fullWidth variant="outlined" sx={{ marginBottom: 3 }}>
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
                onChange={(e) => setTransactionCount(e.target.value)}
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
                onChange={(e) => setThresholdAmount(e.target.value)}
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
                onChange={(e) => setMaxTransactionAmount(e.target.value)}
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
