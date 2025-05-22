import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'Manager',
            component: () => import('@/views/Manager.vue'),
            redirect: '/home',
            children: [
                {path: '403', name: '403', component: () => import('@/views/manager/403.vue')},
                {path: 'password', name: 'Password', component: () => import('@/views/manager/Password.vue')},
                {path: 'home', name: 'Home', component: () => import('@/views/manager/Home.vue')},
                {path: 'course', name: 'Course', component: () => import('@/views/manager/Course.vue')},
                {path: 'student', name: 'Student', component: () => import('@/views/manager/Student.vue')},
                {path: 'person', name: 'Person', component: () => import('@/views/manager/Person.vue')},
                {path: 'user', name: 'User', component: () => import('@/views/manager/User.vue')},
                {path: 'courseList', name: 'CourseList', component: () => import('@/views/manager/CourseList.vue')},
                {path: 'studentCourse', name: 'StudentCourse', component: () => import('@/views/manager/StudentCourse.vue')},
                {path: 'grade', name: 'Grade', component: () => import('@/views/manager/Grade.vue')},
                {path: 'news', name: 'News', component: () => import('@/views/manager/News.vue')},
                {path: 'newsDetail', name: 'NewsDetail', component: () => import('@/views/manager/NewsDetail.vue')},
                {path: 'notice', name: 'Notice', component: () => import('@/views/manager/Notice.vue')},
                {path: 'logs', name: 'Logs', component: () => import('@/views/manager/Logs.vue')},
                {path: 'charts', name: 'Charts', component: () => import('@/views/manager/Charts.vue')},
                {path: 'charts1', name: 'Charts1', component: () => import('@/views/manager/Charts1.vue')},
                {path: 'echarts', name: 'Echarts', component: () => import('@/views/manager/Echarts.vue')},
                {path: 'orders', name: 'Orders', component: () => import('@/views/manager/Orders.vue')},
                {path: 'article', name: 'Article', component: () => import('@/views/manager/Article.vue')},
            ]
        },
        {path: '/login', name: 'Login', component: () => import('@/views/Login.vue'),},
        {path: '/register', name: 'Register', component: () => import('@/views/Register.vue'),},
        {path: '/:pathMatch(.*)', name: '404', component: () => import('@/views/404.vue'),},
    ]
})

router.beforeEach((to, from, next) => {
    // to �ǵ����·����Ϣ
    // from �ǿ�Դ��·����Ϣ
    // next �ǰ���������ת·�ɵĺ���
    let adminPaths = ['/course']
    let user = JSON.parse(localStorage.getItem('student-user') || '{}')

    if (user.role !== 'ADMIN' && adminPaths.includes(to.path)) {
        // �����ǰ��¼���û����ǹ���Ա��Ȼ��ǰ�ĵ����·���ǹ���Ա����Ȩ�޷��ʵ�·���������ʱ���Ҿ����û�ȥ��һ��û��Ȩ�޵�ҳ�棬����������ʵ�ʵ�ҳ��
        next('/403')
    } else {
        next()
    }
})


export default router
