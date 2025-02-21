import React, { useMemo } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  PieChart,
  Pie,
  Cell,
} from "recharts";

const COLORS = [
  "#0088FE",
  "#00C49F",
  "#FFBB28",
  "#FF8042",
  "#8884d8",
  "#82ca9d",
  "#ffc658",
];

const TransactionChart = ({ transactions }) => {
  const chartData = useMemo(() => {
    // 按類別分組統計
    const categoryStats = transactions.reduce((acc, curr) => {
      const key = curr.category;
      if (!acc[key]) {
        acc[key] = {
          category: key,
          收入: 0,
          支出: 0,
        };
      }
      if (curr.type === "INCOME") {
        acc[key].收入 += curr.amount;
      } else {
        acc[key].支出 += curr.amount;
      }
      return acc;
    }, {});

    return Object.values(categoryStats);
  }, [transactions]);

  const pieData = useMemo(() => {
    const expensesByCategory = transactions
      .filter((t) => t.type === "EXPENSE")
      .reduce((acc, curr) => {
        if (!acc[curr.category]) {
          acc[curr.category] = 0;
        }
        acc[curr.category] += curr.amount;
        return acc;
      }, {});

    return Object.entries(expensesByCategory).map(([name, value]) => ({
      name,
      value,
    }));
  }, [transactions]);

  return (
    <div className="card">
      <div className="card-body">
        <h5 className="card-title mb-4">收支分析</h5>
        <div style={{ height: "300px" }}>
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="category" />
              <YAxis />
              <Tooltip
                formatter={(value) =>
                  new Intl.NumberFormat("zh-TW", {
                    style: "currency",
                    currency: "TWD",
                    minimumFractionDigits: 0,
                  }).format(value)
                }
              />
              <Legend />
              <Bar dataKey="收入" fill="#82ca9d" />
              <Bar dataKey="支出" fill="#ff7875" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <h5 className="card-title mt-4 mb-4">支出分布</h5>
        <div style={{ height: "300px" }}>
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie
                data={pieData}
                cx="50%"
                cy="50%"
                labelLine={true}
                label={({ name, percent }) =>
                  `${name} (${(percent * 100).toFixed(0)}%)`
                }
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {pieData.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip
                formatter={(value) =>
                  new Intl.NumberFormat("zh-TW", {
                    style: "currency",
                    currency: "TWD",
                    minimumFractionDigits: 0,
                  }).format(value)
                }
              />
            </PieChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default TransactionChart;
