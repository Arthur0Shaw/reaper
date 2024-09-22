"use client"; // Add this line to ensure it's a client-side component

import React, { useEffect, useState } from "react";
import { Grid } from "@mui/material";
import ProfitExpenses from "@/app/(DashboardLayout)/components/dashboard/ProfitExpenses";
import TrafficDistribution from "@/app/(DashboardLayout)/components/dashboard/TrafficDistribution";
import ProductSales from "@/app/(DashboardLayout)/components/dashboard/ProductSales";
import fetchDashboardData from "@/utils/dashboardService";

const Dashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [totalCount, setTotalCount] = useState(0);
  const [totalAmount, setTotalAmount] = useState(0);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Update total count and total amount based on API response
  const updateTotals = (data) => {
    const totalTransactionCount = Object.values(data.dateWiseTransactionCount || {}).reduce((acc, val) => acc + val, 0);
    const totalTransactionAmount = Object.values(data.dateWiseTransactionAmount || {}).reduce((acc, val) => acc + val, 0);

    setTotalCount(totalTransactionCount);
    setTotalAmount(totalTransactionAmount);
  };

  useEffect(() => {
    const loadDashboardData = async () => {
      try {
        const data = await fetchDashboardData();
        setDashboardData(data);
        updateTotals(data); // Update total count and amount
        setLoading(false);
      } catch (err) {
        console.error("Error fetching dashboard data:", err);
        setError(err.message);
        setLoading(false);
      }
    };

    loadDashboardData();
  }, []);

  if (loading) {
    return <div>Loading dashboard data...</div>;
  }

  if (error) {
    return <div>Error: {error}</div>;
  }

  if (!dashboardData || !dashboardData.dateWiseTransactionCount || !dashboardData.dateWiseTransactionAmount) {
    return <div>No dashboard data available</div>;
  }

  return (
    <Grid container spacing={3}>
      <Grid item xs={12} lg={8}>
        <ProfitExpenses
          dateWiseTransactionCount={dashboardData.dateWiseTransactionCount}
          dateWiseTransactionAmount={dashboardData.dateWiseTransactionAmount}
          onFilterChange={updateTotals} // Ensure child updates parent state
        />
      </Grid>

      <Grid item xs={12} lg={4}>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <TrafficDistribution totalCount={totalCount} />
          </Grid>

          <Grid item xs={12}>
            <ProductSales totalAmount={totalAmount} />
          </Grid>
        </Grid>
      </Grid>
    </Grid>
  );
};

export default Dashboard;
