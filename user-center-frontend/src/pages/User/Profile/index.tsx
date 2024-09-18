import ProCard from '@ant-design/pro-card';
import { PageContainer } from '@ant-design/pro-components';
import { Helmet } from '@umijs/max';
import React from 'react';
import UserProfileForm from './UserProfileForm';

const Profile: React.FC = () => {
  return (
    <PageContainer>
      <Helmet>
        <title>User Profile</title>
      </Helmet>
      <ProCard layout={'center'}>
        <UserProfileForm />
      </ProCard>
    </PageContainer>
  );
};
export default Profile;
