/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(initialState: { currentUser?: API.AdminModel } | undefined) {
  // const { currentUser, isAdmin, hasRoutes = [] } = initialState ?? {};
  // const { currentUser } = initialState ?? {};
  // return {
  //   canAdmin: currentUser && currentUser.access === 'admin',
  // };
  return true
}
