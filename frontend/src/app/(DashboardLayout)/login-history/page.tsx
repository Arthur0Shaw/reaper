// src/app/(DashboardLayout)/login-history/page.tsx

"use client";

import React from "react";
import { Box } from "@mui/material";
import LoginHistoryTable from "../components/login/LoginHistoryTable";

const LoginHistoryPage = () => {
  return (
    <Box>
      <LoginHistoryTable />
    </Box>
  );
};

export default LoginHistoryPage;
