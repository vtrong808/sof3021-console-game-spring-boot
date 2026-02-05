document.addEventListener('DOMContentLoaded', () => {
    const canvas = document.getElementById('revenueChart');
    if (!canvas) return;

    // 1. Lấy dữ liệu từ Thymeleaf
    /*[[${monthlyRevenues}]]*/
    const revenueData = /*[[${monthlyRevenues}]]*/[
        // Dữ liệu giả lập để test giao diện khi không chạy server
        { month: 1, revenue: 12 }, { month: 2, revenue: 19 }, { month: 3, revenue: 8 },
        { month: 4, revenue: 15 }, { month: 5, revenue: 22 }, { month: 6, revenue: 30 },
        { month: 7, revenue: 28 }, { month: 8, revenue: 35 }, { month: 9, revenue: 40 },
        { month: 10, revenue: 38 }, { month: 11, revenue: 45 }, { month: 12, revenue: 50 }
    ];

    // 2. Map dữ liệu
    const labels = (revenueData || []).map(r => "Tháng " + r.month);
    const values = (revenueData || []).map(r => r.revenue);

    // 3. Khởi tạo Chart (Type: LINE, Logic: CŨ)
    new Chart(canvas, {
        type: 'line', // Đổi thành line theo yêu cầu mới
        data: {
            labels: labels,
            datasets: [{
                label: 'Doanh thu (triệu ₫)',
                data: values,
                tension: 0.3, // Bo cong đường biểu đồ

                // Giữ lại màu sắc và styling từ logic cũ nhưng điều chỉnh cho Line chart
                backgroundColor: 'rgba(34, 197, 94, 0.1)', // Màu nền mờ dưới đường line
                borderColor: '#056e2c', // Màu đường line (giống màu viền cột cũ)
                borderWidth: 2,
                fill: true, // Tô màu dưới đường line

                // Styling cho điểm dữ liệu (point)
                pointBackgroundColor: '#fff',
                pointBorderColor: '#056e2c',
                pointHoverBackgroundColor: '#056e2c',
                pointHoverBorderColor: '#fff',
                pointRadius: 4,
                pointHoverRadius: 6
            }]
        },
        // Giữ nguyên toàn bộ options logic từ đoạn code cũ
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: {
                duration: 1500,
                easing: 'easeOutQuart'
            },
            plugins: {
                legend: {
                    position: 'top',
                    labels: {
                        color: '#334155',
                        font: { size: 14, weight: '600' }
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(25, 150, 71, 0.95)',
                    titleColor: '#fff',
                    bodyColor: '#fff',
                    cornerRadius: 8,
                    padding: 12,
                    callbacks: {
                        label: (ctx) =>
                            `${ctx.parsed.y.toLocaleString('vi-VN')} triệu ₫`
                    }
                }
            },
            scales: {
                x: {
                    grid: { display: false },
                    ticks: {
                        color: '#64748b',
                        font: { size: 13 }
                    }
                },
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0,0,0,0.05)',
                        drawBorder: false
                    },
                    ticks: {
                        color: '#64748b',
                        callback: (value) => value + ' triệu'
                    }
                }
            }
        }
    });
});