import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './user-report.reducer';

export const UserReport = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userReportList = useAppSelector(state => state.userReport.entities);
  const loading = useAppSelector(state => state.userReport.loading);
  const totalItems = useAppSelector(state => state.userReport.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="user-report-heading" data-cy="UserReportHeading">
        <Translate contentKey="monsterdamChicksApp.userReport.home.title">User Reports</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="monsterdamChicksApp.userReport.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-report/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="monsterdamChicksApp.userReport.home.createLabel">Create new User Report</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userReportList && userReportList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('reportDescription')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.reportDescription">Report Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportDescription')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedBy')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.lastModifiedBy">Last Modified By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedBy')} />
                </th>
                <th className="hand" onClick={sort('deletedDate')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.deletedDate">Deleted Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedDate')} />
                </th>
                <th className="hand" onClick={sort('reportCategory')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.reportCategory">Report Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportCategory')} />
                </th>
                <th className="hand" onClick={sort('reporterId')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.reporterId">Reporter Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reporterId')} />
                </th>
                <th className="hand" onClick={sort('reportedId')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.reportedId">Reported Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reportedId')} />
                </th>
                <th className="hand" onClick={sort('multimediaId')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.multimediaId">Multimedia Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('multimediaId')} />
                </th>
                <th className="hand" onClick={sort('messageId')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.messageId">Message Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('messageId')} />
                </th>
                <th className="hand" onClick={sort('postId')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.postId">Post Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postId')} />
                </th>
                <th className="hand" onClick={sort('commentId')}>
                  <Translate contentKey="monsterdamChicksApp.userReport.commentId">Comment Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('commentId')} />
                </th>
                <th>
                  <Translate contentKey="monsterdamChicksApp.userReport.ticket">Ticket</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userReportList.map((userReport, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-report/${userReport.id}`} color="link" size="sm">
                      {userReport.id}
                    </Button>
                  </td>
                  <td>{userReport.reportDescription}</td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.ReportStatus.${userReport.status}`} />
                  </td>
                  <td>
                    {userReport.createdDate ? <TextFormat type="date" value={userReport.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userReport.lastModifiedDate ? (
                      <TextFormat type="date" value={userReport.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userReport.createdBy}</td>
                  <td>{userReport.lastModifiedBy}</td>
                  <td>
                    {userReport.deletedDate ? <TextFormat type="date" value={userReport.deletedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    <Translate contentKey={`monsterdamChicksApp.ReportCategory.${userReport.reportCategory}`} />
                  </td>
                  <td>{userReport.reporterId}</td>
                  <td>{userReport.reportedId}</td>
                  <td>{userReport.multimediaId}</td>
                  <td>{userReport.messageId}</td>
                  <td>{userReport.postId}</td>
                  <td>{userReport.commentId}</td>
                  <td>{userReport.ticket ? <Link to={`/assistance-ticket/${userReport.ticket.id}`}>{userReport.ticket.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-report/${userReport.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-report/${userReport.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/user-report/${userReport.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="monsterdamChicksApp.userReport.home.notFound">No User Reports found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userReportList && userReportList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default UserReport;
