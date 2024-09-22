import React from "react";
import { Grid, Stack, Typography, Avatar, Box } from "@mui/material";
import { IconCheck, IconX, IconClock } from "@tabler/icons-react";
import DashboardCard from "@/app/(DashboardLayout)/components/shared/DashboardCard";
import dynamic from "next/dynamic";
import { useTheme } from "@mui/material/styles";

const Chart = dynamic(() => import("react-apexcharts"), { ssr: false });

const TrafficDistribution = ({ totalCount }) => {
  const theme = useTheme();

  // Dynamic values for success, failure, and pending based on totalCount
  const successCount = totalCount * 0.7; // Example: 70% success
  const failureCount = totalCount * 0.2; // Example: 20% failure
  const pendingCount = totalCount * 0.1; // Example: 10% pending

  // Minimalistic color palette
  const successColor = "#66bb6a"; // Subtle green for success
  const failureColor = "#ef5350"; // Subtle red for failure
  const pendingColor = "#ffa726"; // Subtle orange for pending

  // Chart options with minimalistic colors and labels
  const chartOptions = {
    chart: {
      type: "donut",
      fontFamily: "'Plus Jakarta Sans', sans-serif;",
      foreColor: "#adb0bb",
      toolbar: { show: false },
      height: 170,
    },
    colors: [successColor, failureColor, pendingColor], // Minimalistic color scheme
    plotOptions: {
      pie: {
        startAngle: 0,
        endAngle: 360,
        donut: {
          size: "75%",
          background: "transparent",
        },
      },
    },
    labels: ["Success", "Failure", "Pending"], // Labels for chart
    stroke: { show: false },
    dataLabels: { enabled: false },
    tooltip: {
      theme: theme.palette.mode === "dark" ? "dark" : "light",
      fillSeriesColor: false,
    },
    legend: { show: false }, // Hide legend for a minimalistic look
  };

  const chartSeries = [successCount, failureCount, pendingCount]; // Dynamic values for the chart

  return (
    <DashboardCard title="Total Count">
      <Grid container spacing={3}>
        {/* Transaction Data */}
        <Grid item xs={6} sm={7}>
          <Typography variant="h3" fontWeight="700">
            {totalCount}
          </Typography>

          {/* Display success, failure, and pending stats */}
          <Stack spacing={2} mt={2}>
            <Stack direction="row" alignItems="center" spacing={1}>
              <Avatar sx={{ bgcolor: successColor, width: 24, height: 24 }}>
                <IconCheck width={16} color="#ffffff" />
              </Avatar>
              <Typography variant="subtitle2" fontWeight="600">
                {Math.round(successCount)}
              </Typography>
            </Stack>

            <Stack direction="row" alignItems="center" spacing={1}>
              <Avatar sx={{ bgcolor: failureColor, width: 24, height: 24 }}>
                <IconX width={16} color="#ffffff" />
              </Avatar>
              <Typography variant="subtitle2" fontWeight="600">
                {Math.round(failureCount)}
              </Typography>
            </Stack>

            <Stack direction="row" alignItems="center" spacing={1}>
              <Avatar sx={{ bgcolor: pendingColor, width: 24, height: 24 }}>
                <IconClock width={16} color="#ffffff" />
              </Avatar>
              <Typography variant="subtitle2" fontWeight="600">
                {Math.round(pendingCount)}
              </Typography>
            </Stack>
          </Stack>
        </Grid>

        {/* Donut Chart */}
        <Grid item xs={6} sm={5}>
          <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <Chart
              options={chartOptions}
              series={chartSeries}
              type="donut"
              width="100%" 
              height="140px"
            />
          </Box>
        </Grid>
      </Grid>
    </DashboardCard>
  );
};

export default TrafficDistribution;
