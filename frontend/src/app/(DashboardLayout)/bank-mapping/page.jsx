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
  IconButton,
  InputAdornment,
  Divider,
  Paper,
} from "@mui/material";
import { Add, Remove, AccountBalance, Lock, VpnKey, Fingerprint } from "@mui/icons-material"; // Icons

const BankVPASetupPage = () => {
  const [selectedBank, setSelectedBank] = useState(""); // Bank dropdown state
  const [loginID, setLoginID] = useState("");
  const [password, setPassword] = useState("");
  const [adf1, setAdf1] = useState("");
  const [adf2, setAdf2] = useState("");
  const [vpaCount, setVpaCount] = useState(1); // VPA count

  // Bank options
  const bankOptions = ["Yes Bank", "SBI", "HDFC", "ICICI", "BOB", "Bandhan Bank", "IDFC", "Axis Bank"];

  // Handle dropdown change
  const handleBankChange = (event) => {
    setSelectedBank(event.target.value);
  };

  const handleSave = () => {
    if (!selectedBank || !transactionCount || !thresholdAmount || !maxTransactionAmount) {
      toast.error('Please fill out all fields');
    } else {
      toast.success('Routing details saved successfully!');
      // Save logic here
    }
  };


  // Handle incrementing and decrementing VPA count
  const incrementVPA = () => {
    setVpaCount(vpaCount + 1);
  };

  const decrementVPA = () => {
    if (vpaCount > 1) setVpaCount(vpaCount - 1);
  };

  return (
    <Box >
      <Paper
        elevation={3}
        sx={{
          padding: 3,
          backgroundColor: "#black",
          borderRadius: "16px",
          boxShadow: "0 4px 10px rgba(0, 0, 0, 0.05)",
        }}
      >
        <Grid container spacing={4}>
          {/* Left Side - Bank Dropdown and Inputs */}
          <Grid item xs={12} md={6}>
            <Box p={6}>
              <Typography variant="h5" gutterBottom sx={{ fontWeight: "bold", color: "#1f4cb6" }}>
                Bank and Credentials
              </Typography>
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

              <TextField
                fullWidth
                label="Login ID"
                variant="outlined"
                value={loginID}
                onChange={(e) => setLoginID(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <VpnKey />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              <TextField
                fullWidth
                label="Password"
                variant="outlined"
                value={password}
                type="password"
                onChange={(e) => setPassword(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Lock />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              <TextField
                fullWidth
                label="ADF1"
                variant="outlined"
                value={adf1}
                onChange={(e) => setAdf1(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Fingerprint />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />

              <TextField
                fullWidth
                label="ADF2"
                variant="outlined"
                value={adf2}
                onChange={(e) => setAdf2(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <Fingerprint />
                    </InputAdornment>
                  ),
                }}
                sx={{ marginBottom: 3 }}
              />
            </Box>
          </Grid>

          {/* Divider between sections */}
          <Divider orientation="vertical" flexItem />

          {/* Right Side - VPA Input and Increment/Decrement */}
          <Grid item xs={12} md={5}>
            <Box p={8}>
              <Typography variant="h5" gutterBottom sx={{ fontWeight: "bold", color: "#1f4cb6" }}>
                Virtual Payment Address (VPA)
              </Typography>

              {Array.from({ length: vpaCount }, (_, index) => (
                <TextField
                  key={index}
                  fullWidth
                  label={`VPA ${index + 1}`}
                  variant="outlined"
                  sx={{ marginBottom: 3 }}
                />
              ))}

              {/* VPA Increment and Decrement Buttons */}
              <Box display="flex" alignItems="center" justifyContent="flex-start" gap={2}>
                <IconButton
                  onClick={decrementVPA}
                  color="error"
                  sx={{ backgroundColor: "#ffcccb", ":hover": { backgroundColor: "#f99" } }}
                >
                  <Remove />
                </IconButton>
                <Typography variant="h6" sx={{ fontWeight: "500" }}>
                  VPA Count: {vpaCount}
                </Typography>
                <IconButton
                  onClick={incrementVPA}
                  color="primary"
                  sx={{ backgroundColor: "#d1eaff", ":hover": { backgroundColor: "#80c6ff" } }}
                >
                  <Add />
                </IconButton>
              </Box>
            </Box>
          </Grid>
        </Grid>
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
                Save Your Mapping Details
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
      </Paper>
    </Box>
  );
};

export default BankVPASetupPage;
