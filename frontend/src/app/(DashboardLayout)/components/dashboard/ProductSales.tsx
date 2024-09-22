import React from "react";
import { Stack, Typography, Avatar, Fab, Box } from "@mui/material";
import { IconCurrencyRupee, IconCheck, IconX, IconClock } from "@tabler/icons-react";
import DashboardCard from "@/app/(DashboardLayout)/components/shared/DashboardCard";
import dynamic from "next/dynamic";
import { useTheme } from "@mui/material/styles";

const Chart = dynamic(() => import("react-apexcharts"), { ssr: false });

const ProductSales = ({ totalAmount }) => {
  const theme = useTheme();
  const primary = theme.palette.primary.main;

  // Random values for success, failure, and pending amounts
  const successAmount = totalAmount * 0.7; // 70% of the total amount
  const failureAmount = totalAmount * 0.2; // 20% of the total amount
  const pendingAmount = totalAmount * 0.1; // 10% of the total amount

  // Data for the sparkline chart
  const optionscolumnchart = {
    chart: {
      type: "area",
      fontFamily: "'Plus Jakarta Sans', sans-serif;",
      foreColor: "#adb0bb",
      toolbar: { show: false },
      height: 60,
      sparkline: { enabled: true },
    },
    stroke: { curve: "smooth", width: 2 },
    fill: { colors: [primary], type: "solid", opacity: 0.05 },
    markers: { size: 0 },
    tooltip: {
      theme: theme.palette.mode === "dark" ? "dark" : "light",
    },
  };

  const seriescolumnchart = [
    {
      name: "Total Amount",
      data: [totalAmount, totalAmount * 1.1, totalAmount * 0.9, totalAmount * 1.05, totalAmount * 0.95],
    },
  ];

  return (
    <DashboardCard
      title="Total Amount"
      action={
        <Fab color="primary" size="medium" sx={{ color: "#ffffff" }}>
          <IconCurrencyRupee width={24} />
        </Fab>
      }
      footer={
        <Chart options={optionscolumnchart} series={seriescolumnchart} type="area" width={"100%"} height="150px" />
      }
    >
      <>
        <Typography variant="h3" fontWeight="700" mt="-20px">
          ₹{totalAmount}
        </Typography>

        {/* Display success, failure, and pending stats in a vertical stack */}
        <Box sx={{ mt: 2 }}>
          <Stack direction="column" spacing={2}>
            {/* Success */}
            <Stack direction="row" alignItems="center" spacing={1}>
              <Avatar sx={{ bgcolor: "#4caf50", width: 24, height: 24 }}>
                <IconCheck width={16} color="#ffffff" />
              </Avatar>
              <Typography variant="subtitle2" fontWeight="600">
                ₹{successAmount.toFixed(2)}
              </Typography>
            </Stack>

            {/* Failure */}
            <Stack direction="row" alignItems="center" spacing={1}>
              <Avatar sx={{ bgcolor: "#f44336", width: 24, height: 24 }}>
                <IconX width={16} color="#ffffff" />
              </Avatar>
              <Typography variant="subtitle2" fontWeight="600">
                ₹{failureAmount.toFixed(2)}
              </Typography>
            </Stack>

            {/* Pending */}
            <Stack direction="row" alignItems="center" spacing={1}>
              <Avatar sx={{ bgcolor: "#ff9800", width: 24, height: 24 }}>
                <IconClock width={16} color="#ffffff" />
              </Avatar>
              <Typography variant="subtitle2" fontWeight="600">
                ₹{pendingAmount.toFixed(2)}
              </Typography>
            </Stack>
          </Stack>
        </Box>
      </>
    </DashboardCard>
  );
};

export default ProductSales;
